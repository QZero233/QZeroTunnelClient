package com.qzero.tunnel.client;

import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.command.CommandExecutor;
import com.qzero.tunnel.client.config.ServerPortInfo;
import com.qzero.tunnel.client.config.ServerProfile;
import com.qzero.tunnel.client.data.UserToken;
import com.qzero.tunnel.client.remind.RemindThread;
import com.qzero.tunnel.client.service.AuthorizeService;
import com.qzero.tunnel.client.utils.HttpUtils;
import com.qzero.tunnel.client.utils.NormalHttpUtils;
import com.qzero.tunnel.client.utils.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientMain {

    private static Logger log= LoggerFactory.getLogger(ClientMain.class);

    private static Scanner scanner=new Scanner(System.in);

    public static final String TOKEN_STORAGE_FILE_NAME="token.storage";
    public static final String SERVER_CONFIG_FILE_NAME="serverConfig.config";
    public static final String SERVERS_PATH="servers/";

    static {
        new File(SERVERS_PATH).mkdirs();
    }

    private static ServerProfile serverProfile;

    public static void main(String[] args) throws Exception{
        serverProfile=chooseServer();

        String baseUrl="http://"+serverProfile.getServerIp()+":"+serverProfile.getEntrancePort();
        HttpUtils.getInstance().setBaseUrl(baseUrl);

        UserToken token=chooseToken();
        System.out.println("Token chosen");
        HttpUtils.getInstance().setAuthInfo(token);

        try {
            ServerPortInfo portInfo= serverProfile.getPortInfo();;
            RemindThread remindThread=new RemindThread(serverProfile.getServerIp(),portInfo.getRemindServerPort()
                    ,portInfo.getRelaySeverPort(),token.getToken());
            remindThread.start();
        }catch (Exception e){
            System.err.println("Failed to connect to remind server");
            log.error("Failed to connect to remind server",e);
            return;
        }


        CommandExecutor executor=CommandExecutor.getInstance();
        executor.loadCommands();

        while (true){
            System.out.print("Command>");
            String line=scanner.nextLine();

            if(line.equalsIgnoreCase("exit")){
                System.out.println("Good-bye");
                System.exit(0);
                break;
            }

            try {
                String result=executor.executeCommand(line);
                System.out.println(result);
            }catch (Exception e){
                log.error("Failed to execute command "+line,e);
                System.out.println("Execute failed, reason: "+e.getMessage());
            }

        }

    }

    private static ServerProfile chooseServer(){
        File[] files=new File(SERVERS_PATH).listFiles();

        List<ServerProfile> profileList=new ArrayList<>();

        if(files==null || files.length==0){
            System.out.println("No server to select");
        }else{
            int i=1;
            for(File file:files){
                File configFile=new File(file,SERVER_CONFIG_FILE_NAME);
                if(!configFile.exists())
                    continue;

                try {
                    String content=StreamUtils.readFileIntoString(configFile);
                    String[] parts=content.split(",");
                    String serverIp=parts[0];
                    int port=Integer.parseInt(parts[1]);

                    ServerPortInfo portInfo= JSONObject.parseObject(NormalHttpUtils.doGet("http://"+serverIp+":"+port+"/server/port_info"),
                            ServerPortInfo.class);

                    ServerProfile profile=new ServerProfile(file.getName(),serverIp,port,portInfo);
                    profileList.add(profile);
                    log.trace("Found server "+file.getName()+" : "+profile);

                    System.out.println(String.format("%d) %s (http://%s:%d/)", i,file.getName(),serverIp,port));
                    i++;
                }catch (Exception e){
                    log.error("Failed to load config for server named "+file.getName());
                }
            }
        }

        while (true){
            System.out.print("Input the index to select server (0 to create a server profile) :");
            int choice=scanner.nextInt();
            if(choice<0 || choice>profileList.size()){
                System.out.println("Illegal input, please check");
                continue;
            }

            if(choice!=0){
                choice--;
                return profileList.get(choice);
            }

            System.out.print("Please input the name of the server:");
            String name=scanner.next();

            if(new File(SERVERS_PATH+name).exists()){
                System.out.println("Server named "+name+" already exists, please choose another name");
                continue;
            }

            System.out.print("Please input the ip of the server:");
            String ip=scanner.next();

            System.out.print("Please input the port of the server:");
            int port=scanner.nextInt();

            //Load port info from remote server
            try {
                ServerPortInfo portInfo= JSONObject.parseObject(NormalHttpUtils.doGet("http://"+ip+":"+port),
                        ServerPortInfo.class);

                ServerProfile profile=new ServerProfile(name,ip,port,portInfo);

                //It's a valid server, make it to file system
                try {
                    new File(SERVERS_PATH+name).mkdirs();
                    File configFle=new File(SERVERS_PATH+name,SERVER_CONFIG_FILE_NAME);
                    if(!configFle.exists())
                        configFle.createNewFile();

                    StreamUtils.writeFile(configFle,(ip+","+port).getBytes());
                }catch (Exception e){
                    log.error("Failed to create profile on file system for server "+name+"("+ip+","+port+")");
                    System.out.println("Failed to save server config on file system, you may need to input it again next time");
                }

                return profile;
            }catch (Exception e){
                System.out.println("Failed to load server port info from remote server, this server profile is dropped");
                log.error("Failed to load config for server "+name+"("+ip+","+port+")");
                continue;
            }

        }
    }


    private static UserToken chooseToken(){

        while (true){
            System.out.println("1) Login now for token");
            System.out.println("2) Choose token from existing token storage");
            System.out.println("-1) Exit");
            System.out.print("Please input your choice:");

            String choice=scanner.next();
            UserToken token=null;
            switch (choice){
                case "-1":
                    System.out.println("Bye-bye");
                    System.exit(0);
                    break;
                case "1":
                    token=loginForToken();
                    break;
                case "2":
                    token=chooseTokenFromStorage();
                    break;
            }

            if(token==null)
                continue;
            else
                return token;
        }
    }

    private static UserToken loginForToken(){
        System.out.print("Please input your username:");
        String username=scanner.next();
        System.out.print("Please input your password hash:");
        String passwordHash=scanner.next();

        String token= null;
        try {
            token = AuthorizeService.login(username,passwordHash);
        } catch (Exception e) {
            log.error("Failed to login",e);
        }

        if(token!=null){
            try {
                addTokenIntoStorage(token,username);
                System.out.println("Login successfully");
            }catch (Exception e){
                log.error("Failed to save token into token storage",e);
            }
            return new UserToken(token,username);
        }else{
            System.out.println("Login failed");
            return null;
        }
    }

    private static UserToken chooseTokenFromStorage(){
        String storageString;
        try {
            File file=new File(SERVERS_PATH+serverProfile.getServerName(),TOKEN_STORAGE_FILE_NAME);
            byte[] buf= StreamUtils.readFile(file);
            storageString=new String(buf);
            if(storageString==null || storageString.equals(""))
                throw new Exception();
        }catch (Exception e){
            System.out.println("Token storage is empty");
            return null;
        }

        String[] lines=storageString.split("\n");
        List<UserToken> tokenList=new ArrayList<>();
        int i=0;
        for(String line:lines){
            String[] parts=line.split(",");
            if(parts.length!=2)
                continue;
            String token=parts[0];
            String username=parts[1];
            System.out.println(String.format("%d)%s %s", i,username,token));

            tokenList.add(new UserToken(token,username));
            i++;
        }

        System.out.print("Please input your choice:");
        int choice=scanner.nextInt();
        if(choice>lines.length-1 || choice<0){
            System.out.println("Illegal input");
            return null;
        }

        return tokenList.get(choice);
    }

    private static void addTokenIntoStorage(String token,String username) throws IOException {
        File file=new File(SERVERS_PATH+serverProfile.getServerName(),TOKEN_STORAGE_FILE_NAME);
        if(!file.exists())
            file.createNewFile();

        byte[] buf=StreamUtils.readFile(file);
        String content=new String(buf);
        content+="\n"+token+","+username;
        StreamUtils.writeFile(file,content.getBytes(StandardCharsets.UTF_8));
    }

}

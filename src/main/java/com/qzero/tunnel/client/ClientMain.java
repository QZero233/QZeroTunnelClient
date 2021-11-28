package com.qzero.tunnel.client;

import com.qzero.tunnel.client.command.CommandExecutor;
import com.qzero.tunnel.client.data.ServerPortInfo;
import com.qzero.tunnel.client.data.ServerProfile;
import com.qzero.tunnel.client.data.UserToken;
import com.qzero.tunnel.client.remind.RemindThread;
import com.qzero.tunnel.client.service.AuthorizeService;
import com.qzero.tunnel.client.service.ServerProfileService;
import com.qzero.tunnel.client.utils.HttpUtils;
import com.qzero.tunnel.client.utils.SHA256Utils;
import com.qzero.tunnel.client.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ClientMain {

    private static Logger log= LoggerFactory.getLogger(ClientMain.class);

    private static Scanner scanner=new Scanner(System.in);

    private static ServerProfile serverProfile;

    public static void main(String[] args) throws Exception{
        SpringApplication.run(ClientMain.class);

        new TestServer().start();

        GlobalConfigStorage configStorage=SpringUtil.getBean(GlobalConfigStorage.class);

        //Choose server profile
        serverProfile=chooseServer();
        configStorage.setCurrentServerProfile(serverProfile);

        //Set base url
        String baseUrl="http://"+serverProfile.getServerIp()+":"+serverProfile.getEntrancePort();
        HttpUtils.getInstance().setBaseUrl(baseUrl);

        //Choose user token
        UserToken token=chooseToken();
        configStorage.setUserToken(token);
        System.out.println("Token chosen");

        //Set auth info
        HttpUtils.getInstance().setAuthInfo(token);

        //Connect to remind server
        try {
            ServerPortInfo portInfo= serverProfile.getPortInfo();;
            RemindThread.initializeInstance(serverProfile.getServerIp(),portInfo.getRemindServerPort()
                    ,portInfo.getRelaySeverPort(),token.getToken());
            RemindThread.getInstance().start();
        }catch (Exception e){
            System.err.println("Failed to connect to remind server");
            log.error("Failed to connect to remind server",e);
            return;
        }

        //Initialize command executor
        CommandExecutor executor=SpringUtil.getBean(CommandExecutor.class);

        //Start command line
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

    /**
     * Output a menu to let user choose server
     * @return
     */
    private static ServerProfile chooseServer(){
        ServerProfileService service=SpringUtil.getBean(ServerProfileService.class);
        List<ServerProfile> profileList=service.getAllAvailableServerProfile();

        //Output choose list
        if(profileList==null || profileList.isEmpty()){
            System.out.println("Server profile database is empty");
        }else{
            int i=1;
            for(ServerProfile profile:profileList){
                System.out.println(String.format("%d) %s (%s:%d)",
                        i,profile.getServerName(),profile.getServerIp(),profile.getEntrancePort()));
                i++;
            }
        }

        //Prompt user to choose
        while (true) {
            System.out.print("Input the index to select server (0 to create a server profile) :");
            int choice = scanner.nextInt();

            //Check if the choice is out of bound
            if (choice < 0 || choice > profileList.size()) {
                System.out.println("Illegal input, please check");
                continue;
            }

            //Legal input, get the result
            if (choice != 0) {
                choice--;
                return profileList.get(choice);
            }

            //Add new server
            System.out.print("Please input the name of the server:");
            String name = scanner.next();

            System.out.print("Please input the ip of the server:");
            String ip = scanner.next();

            System.out.print("Please input the port of the server:");
            int port = scanner.nextInt();

            //Load port info from remote server
            ServerProfile serverProfile = new ServerProfile(UUIDUtils.getRandomUUID(), ip, name, port, null);
            serverProfile = service.getServerPortAndFillServerProfile(serverProfile);

            if (serverProfile == null) {
                //Which means it's not an available server
                System.out.println("Failed to load server port info from remote server, this server profile is dropped");
                continue;
            }

            //Save profile into database and choose it
            service.saveServerProfile(serverProfile);
            return serverProfile;
        }
    }

    /**
     * Output a menu to let user choose token
     * @return
     */
    private static UserToken chooseToken(){

        while (true){
            System.out.println("1) Login now for token");
            System.out.println("2) Register a new account");
            System.out.println("3) Choose token from existing token storage");
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
                    token=register();
                    break;
                case "3":
                    token=chooseTokenFromStorage();
                    break;
            }

            if(token==null)
                continue;
            else
                return token;
        }
    }

    /**
     * Prompt user to register a new account
     * @return
     */
    private static UserToken register(){
        System.out.print("Please input your username:");
        String username=scanner.next();
        System.out.print("Please input your password:");
        String password=scanner.next();

        String passwordHash= SHA256Utils.getHexEncodedSHA256(password);

        try {
            AuthorizeService authorizeService=SpringUtil.getBean(AuthorizeService.class);
            authorizeService.register(username,passwordHash);
            System.out.println("Register successfully, please login");
        }catch (Exception e){
            log.error("Failed to register",e);
            System.out.println("Register failed");
        }

        return null;
    }

    /**
     * Prompt user to login for token
     * If login succeeded, it will add the token into token storage
     * @return
     */
    private static UserToken loginForToken(){
        System.out.print("Please input your username:");
        String username=scanner.next();
        System.out.print("Please input your password:");
        String password=scanner.next();

        String passwordHash= SHA256Utils.getHexEncodedSHA256(password);

        AuthorizeService authorizeService=SpringUtil.getBean(AuthorizeService.class);
        String token= null;
        try {
            token = authorizeService.login(username,passwordHash);
        } catch (Exception e) {
            log.error("Failed to login",e);
        }

        if(token!=null){
            UserToken userToken=new UserToken(token,username,serverProfile.getServerIp());
            authorizeService.addUserToken(userToken);
            return userToken;
        }else{
            System.out.println("Login failed");
            return null;
        }
    }

    /**
     * Output a list of token
     * and let user choose which one to use
     * @return
     */
    private static UserToken chooseTokenFromStorage(){
        AuthorizeService service=SpringUtil.getBean(AuthorizeService.class);

        List<UserToken> tokenList=service.getAllTokenByServerIp(serverProfile.getServerIp());
        if(tokenList==null || tokenList.isEmpty()){
            System.out.println("Token storage is empty");
            return null;
        }

        int i=0;
        for(UserToken token:tokenList){
            System.out.println(String.format("%d)%s %s", i,token.getUsername(),token.getToken()));
            i++;
        }


        System.out.print("Please input your choice:");
        int choice=scanner.nextInt();
        if(choice> tokenList.size()-1 || choice<0){
            System.out.println("Illegal input");
            return null;
        }

        UserToken token=tokenList.get(choice);

        try {
            //Check validity and remove invalid token
            if(!service.checkTokenValidity(token.getToken(),token.getUsername())){
                System.out.println("Token is invalid, please select another");

                service.deleteToken(tokenList.remove(choice).getToken());
                return null;
            }
        }catch (Exception e){
            log.error("Failed to check token validity",e);
            System.out.println("Warning: failed to check token validity, this token may be invalid");
        }

        return token;
    }

}

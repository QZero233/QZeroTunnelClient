package com.qzero.tunnel.client;

import com.qzero.tunnel.client.command.CommandThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Scanner;

public class ClientMain {

    private static Logger log= LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) throws Exception{
        File configFile=new File("config.txt");
        if(!configFile.exists()){
            log.error("Can not find config.txt");
            return;
        }

        String configString=new String(StreamUtils.readFile(configFile));
        String[] parts=configString.split(",");

        String ip=parts[0];
        int port=Integer.parseInt(parts[1]);
        int relayPort=Integer.parseInt(parts[2]);

        String username=parts[3];
        String password=parts[4];

        CommandThread commandThread=new CommandThread(ip,port,relayPort);
        commandThread.start();
        commandThread.execute(String.format("login %s %s", username,password));

        Scanner scanner=new Scanner(System.in);
        while (true){
            System.out.print("Command>");
            String command=scanner.nextLine();

            if(command.equals("exit"))
                break;

            commandThread.execute(command);
        }


    }

}

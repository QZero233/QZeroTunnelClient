package com.qzero.tunnel.client;

import com.qzero.tunnel.client.command.CommandThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ClientMain {

    private static Logger log= LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) throws Exception{
        //new TestServer().start();

        int port,relayPort;
        String ip;
        Scanner scanner=new Scanner(System.in);

        System.out.print("Please enter the ip of tunnel server:");
        ip=scanner.nextLine();

        System.out.print("Please enter the port of tunnel server:");
        port=Integer.parseInt(scanner.nextLine());

        System.out.print("Please enter the port of relay server:");
        relayPort=Integer.parseInt(scanner.nextLine());

        CommandThread commandThread=new CommandThread(ip,port,relayPort);
        commandThread.start();
        while (true){
            System.out.print("Command>");
            String command=scanner.nextLine();

            if(command.equals("exit"))
                break;

            commandThread.execute(command);
        }


    }

}

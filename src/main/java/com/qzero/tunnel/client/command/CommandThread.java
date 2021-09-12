package com.qzero.tunnel.client.command;

import com.qzero.tunnel.client.relay.RelaySession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandThread extends Thread {

    private Logger log= LoggerFactory.getLogger(getClass());

    private Socket socket;

    private BufferedReader br;
    private PrintWriter pw;

    private String ip;
    private int relayServerPort;

    public CommandThread(String ip, int port,int relayServerPort) throws Exception{
        this.ip=ip;
        this.relayServerPort=relayServerPort;
        socket=new Socket(ip,port);
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw=new PrintWriter(socket.getOutputStream());
        log.info("Connected to serverside's command server");
    }

    public void execute(String command){
        pw.println(command);
        pw.flush();
    }

    @Override
    public void run() {
        super.run();

        try {
            while (!isInterrupted()){
                String line=br.readLine();
                if(line==null){
                    interrupt();
                    break;
                }

                processServerOutput(line);
            }
        }catch (Exception e){
            if(isInterrupted()){
                log.info("Closed connection with serverside's command server");
            }else{
                log.error("Failed to read command line from serverside",e);
            }
        }
    }

    private void processServerOutput(String msg){
        log.info("Message from server:"+msg);
        if(msg.startsWith("connect_relay_session")){
            try {
                String[] parts=msg.split(" ");
                RelaySession session=prepareRelay(parts[1],parts[2],parts[3],Integer.parseInt(parts[4]));
                session.startRelay();
            }catch (Exception e){
                log.error("Failed to start relay session with local",e);
            }

        }
    }

    private RelaySession prepareRelay(String tunnelPort,String sessionId,String localIp,int localPort) throws Exception{
        Socket remote=new Socket(ip,relayServerPort);
        Socket local=new Socket(localIp,localPort);

        remote.getOutputStream().write((tunnelPort+" "+sessionId+"\n").getBytes());
        RelaySession session=new RelaySession();
        session.setTunnelClient(remote);
        session.setDirectClient(local);
        return session;
    }

}

package com.qzero.tunnel.client.remind;

import com.qzero.tunnel.client.relay.RelaySession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class RemindThread extends Thread {

    private Logger log= LoggerFactory.getLogger(getClass());

    private Socket socket;

    private BufferedReader br;

    private String ip;
    private int relayServerPort;

    private String token;

    public RemindThread(String ip, int port, int relayServerPort,String token) throws Exception{
        this.ip=ip;
        this.relayServerPort=relayServerPort;
        this.token=token;
        socket=new Socket(ip,port);
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        log.info("Connected to serverside's remind server");
    }

    @Override
    public void run() {
        super.run();

        //Authorize
        try {
            PrintWriter pw=new PrintWriter(socket.getOutputStream());
            pw.println(token);
            pw.flush();

            if(!br.readLine().equalsIgnoreCase("succeeded")){
                throw new Exception("Wrong token");
            }
        }catch (Exception e){
            log.info("Failed to authorize, lost connection",e);
            try {
                br.close();
                socket.close();
            }catch (Exception e1){}
            return;
        }

        try {
            while (!isInterrupted()){
                String line=br.readLine();
                if(line==null){
                    interrupt();
                    break;
                }

                try {
                    String[] parts=line.split(" ");
                    RelaySession session=prepareRelay(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]));
                    session.startRelay();
                }catch (Exception e1){
                    log.error("Failed to start relay session with line "+line,e1);
                }

            }
        }catch (Exception e){
            if(isInterrupted()){
                log.info("Closed connection with serverside's remind server");
            }else{
                log.error("Failed to read command line from serverside",e);
            }
        }
    }

    private RelaySession prepareRelay(String tunnelPort,String sessionId,String localIp,int localPort) throws Exception{
        Socket remote=new Socket(ip,relayServerPort);
        remote.getOutputStream().write((tunnelPort+" "+sessionId+"\n").getBytes());

        Socket local;
        try {
            local=new Socket(localIp,localPort);
        }catch (ConnectException e){
            remote.close();
            throw e;
        }

        RelaySession session=new RelaySession();
        session.setTunnelClient(remote);
        session.setDirectClient(local);
        return session;
    }

}

package com.qzero.tunnel.client.remind;

import com.qzero.tunnel.crypto.CryptoModule;
import com.qzero.tunnel.crypto.CryptoModuleFactory;
import com.qzero.tunnel.crypto.modules.PlainModule;
import com.qzero.tunnel.relay.RelaySession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class RemindThread extends Thread {

    private Logger log= LoggerFactory.getLogger(getClass());

    private Socket socket;

    private BufferedReader br;

    private static String ip;
    private static int port;
    private static int relayServerPort;

    private static String token;

    private static RemindThread instance;

    public static void initializeInstance(String ip, int port, int relayServerPort,String token) throws Exception{
        instance=new RemindThread(ip,port,relayServerPort,token);
    }

    public static void renewInstance() throws Exception{
        instance.closeConnection();
        instance=new RemindThread(ip,port,relayServerPort,token);
    }

    public static RemindThread getInstance() {
        return instance;
    }

    private RemindThread(String ip, int port, int relayServerPort, String token) throws Exception{
        this.ip=ip;
        this.port=port;
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
                    RelaySession session=prepareRelay(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]),parts[4]);
                    session.startRelay();
                }catch (Exception e1){
                    log.error("Failed to start relay session with line "+line,e1);

                    System.err.println("Failed to start NAT traverse relay session, please check local server or remote server\n" +
                            "reason:"+e1.getMessage());
                }

            }
        }catch (Exception e){
            if(isInterrupted()){
                log.info("Closed connection with serverside's remind server");
            }else{
                log.error("Failed to read command line from serverside",e);
                System.err.println("Lost connection with NAT Traverse remind server, please use command reconnect_to_remind_server to reconnect manually");
            }
        }
    }

    private RelaySession prepareRelay(String tunnelPort,String sessionId,String localIp,int localPort,String cryptoModuleName) throws Exception{
        CryptoModule module= CryptoModuleFactory.getModule(cryptoModuleName);
        if(module==null){
            throw new Exception(String.format("Crypto module named %s does not exist", cryptoModuleName));
        }

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

        try {
            module.doHandshakeAsClient(remote.getInputStream(),remote.getOutputStream());
        }catch (Exception e){
            log.error("Failed to do handshake, session closed",e);
            session.closeSession();
            throw e;
        }

        session.initializeCryptoModule(module,new PlainModule());

        return session;
    }

    public void closeConnection(){
        interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Failed to close connection with remind server",e);
        }
    }

}

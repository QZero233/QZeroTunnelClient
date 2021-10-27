package com.qzero.tunnel.client.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class Socks5ServerAcceptThread extends Thread {

    private int port;

    private ServerSocket serverSocket;

    private Logger log= LoggerFactory.getLogger(getClass());

    private ServerStatusListener serverStatusListener;
    private NewClientConnectedCallback newClientConnectedCallback;

    public Socks5ServerAcceptThread(int port,ServerStatusListener serverStatusListener,NewClientConnectedCallback callback) throws Exception{
        this.port=port;
        this.serverStatusListener=serverStatusListener;
        this.newClientConnectedCallback=callback;

        serverSocket=new ServerSocket(port);
        serverStatusListener.onStarted();
        log.info(String.format("Socks5 server started on port %d successfully", port));
    }

    public void setServerStatusListener(ServerStatusListener serverStatusListener) {
        this.serverStatusListener = serverStatusListener;
    }

    @Override
    public void run() {
        super.run();

        try {
            while (!isInterrupted()){
                Socket socket=serverSocket.accept();
                log.trace(String.format("New client %s:%d connected, ready to handshake", socket.getInetAddress().getHostAddress(),
                        socket.getPort()));
                new Socks5ClientHandshakeThread(socket,newClientConnectedCallback).start();
            }
        }catch (Exception e){
            log.error("Socks5 server closed due to an exception",e);
            serverStatusListener.onStopped();
        }
    }

    public void closeServer() throws Exception{
        interrupt();
        try {
            if(serverSocket!=null && !serverSocket.isClosed())
                serverSocket.close();
        }catch (Exception e){
            log.error("Failed to close socks5 server socket on port "+port);
            throw e;
        }
        serverStatusListener.onStopped();
        log.info(String.format("Socks5 server on port %d has been closed", port));
    }


}

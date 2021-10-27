package com.qzero.tunnel.client.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public class Socks5ClientHandshakeThread extends Thread{

    private Socket socket;

    private Logger log= LoggerFactory.getLogger(getClass());

    private NewClientConnectedCallback callback;

    public Socks5ClientHandshakeThread(Socket socket,NewClientConnectedCallback callback) {
        this.socket = socket;
        this.callback=callback;
    }

    @Override
    public void run() {
        super.run();

        Socks5HandshakeInfo handshakeInfo;
        try {
            handshakeInfo=Socks5HandshakeUtil.doHandshake(socket);
        } catch (Exception e) {
            log.error(String.format("Failed to do handshake with client %s:%d", socket.getInetAddress().getHostAddress(),
                    socket.getPort()));
            return;
        }

        Socks5Connection connection=new Socks5Connection(handshakeInfo,socket);
        callback.onNewClientConnected(socket.getInetAddress().getHostAddress()+":"+socket.getPort(),connection);
    }
}

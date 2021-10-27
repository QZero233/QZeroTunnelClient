package com.qzero.tunnel.client.socks;

import java.net.Socket;

public class Socks5Connection {

    private Socks5HandshakeInfo handshakeInfo;
    private Socket localSocket;

    public Socks5Connection(Socks5HandshakeInfo handshakeInfo, Socket localSocket) {
        this.handshakeInfo = handshakeInfo;
        this.localSocket = localSocket;
    }

    public Socks5Connection() {
    }

    public Socks5HandshakeInfo getHandshakeInfo() {
        return handshakeInfo;
    }

    public void setHandshakeInfo(Socks5HandshakeInfo handshakeInfo) {
        this.handshakeInfo = handshakeInfo;
    }

    public Socket getLocalSocket() {
        return localSocket;
    }

    public void setLocalSocket(Socket localSocket) {
        this.localSocket = localSocket;
    }

    @Override
    public String toString() {
        return "Socks5Connection{" +
                "handshakeInfo=" + handshakeInfo +
                ", localSocket=" + localSocket +
                '}';
    }
}

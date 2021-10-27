package com.qzero.tunnel.client.socks;

public interface NewClientConnectedCallback {

    void onNewClientConnected(String clientId,Socks5Connection connection);

}

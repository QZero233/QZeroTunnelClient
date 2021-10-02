package com.qzero.tunnel.client.data;


public class TunnelConfig {

    private int tunnelPort;
    private String localIp;
    private int localPort;

    public TunnelConfig() {
    }

    public TunnelConfig(int tunnelPort, String localIp, int localPort) {
        this.tunnelPort = tunnelPort;
        this.localIp = localIp;
        this.localPort = localPort;
    }

    public int getTunnelPort() {
        return tunnelPort;
    }

    public void setTunnelPort(int tunnelPort) {
        this.tunnelPort = tunnelPort;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    @Override
    public String toString() {
        return "TunnelConfig{" +
                "tunnelPort=" + tunnelPort +
                ", localIp='" + localIp + '\'' +
                ", localPort=" + localPort +
                '}';
    }
}

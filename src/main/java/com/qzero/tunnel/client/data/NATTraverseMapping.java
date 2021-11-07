package com.qzero.tunnel.client.data;


public class NATTraverseMapping {

    private int tunnelPort;
    private String localIp;
    private int localPort;

    public NATTraverseMapping() {
    }

    public NATTraverseMapping(int tunnelPort, String localIp, int localPort) {
        this.tunnelPort = tunnelPort;
        this.localIp = localIp;
        this.localPort = localPort;
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

    public int getTunnelPort() {
        return tunnelPort;
    }

    public void setTunnelPort(int tunnelPort) {
        this.tunnelPort = tunnelPort;
    }

    @Override
    public String toString() {
        return "NATTraverseMapping{" +
                "tunnelPort=" + tunnelPort +
                ", localIp='" + localIp + '\'' +
                ", localPort=" + localPort +
                '}';
    }
}

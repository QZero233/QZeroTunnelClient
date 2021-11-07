package com.qzero.tunnel.client.data;


public class TunnelConfig {

    private int tunnelPort;
    private String cryptoModuleName;
    private int tunnelType;

    public static final int TYPE_NAT_TRAVERSE=1;
    public static final int TYPE_PROXY=2;

    public TunnelConfig() {
    }

    public TunnelConfig(int tunnelPort, String cryptoModuleName, int tunnelType) {
        this.tunnelPort = tunnelPort;
        this.cryptoModuleName = cryptoModuleName;
        this.tunnelType = tunnelType;
    }

    public int getTunnelPort() {
        return tunnelPort;
    }

    public void setTunnelPort(int tunnelPort) {
        this.tunnelPort = tunnelPort;
    }

    public String getCryptoModuleName() {
        return cryptoModuleName;
    }

    public void setCryptoModuleName(String cryptoModuleName) {
        this.cryptoModuleName = cryptoModuleName;
    }

    public int getTunnelType() {
        return tunnelType;
    }

    public void setTunnelType(int tunnelType) {
        this.tunnelType = tunnelType;
    }

    @Override
    public String toString() {
        return "TunnelConfig{" +
                "tunnelPort=" + tunnelPort +
                ", cryptoModuleName='" + cryptoModuleName + '\'' +
                ", tunnelType=" + tunnelType +
                '}';
    }
}

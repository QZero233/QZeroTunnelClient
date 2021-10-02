package com.qzero.tunnel.client.config;

public class ServerProfile {

    private String serverName;
    private String serverIp;
    private int entrancePort;
    private ServerPortInfo portInfo;

    public ServerProfile() {
    }

    public ServerProfile(String serverName, String serverIp, int entrancePort, ServerPortInfo portInfo) {
        this.serverName = serverName;
        this.serverIp = serverIp;
        this.entrancePort = entrancePort;
        this.portInfo = portInfo;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getEntrancePort() {
        return entrancePort;
    }

    public void setEntrancePort(int entrancePort) {
        this.entrancePort = entrancePort;
    }

    public ServerPortInfo getPortInfo() {
        return portInfo;
    }

    public void setPortInfo(ServerPortInfo portInfo) {
        this.portInfo = portInfo;
    }

    @Override
    public String toString() {
        return "ServerProfile{" +
                "serverIp='" + serverIp + '\'' +
                ", entrancePort=" + entrancePort +
                ", portInfo=" + portInfo +
                '}';
    }
}

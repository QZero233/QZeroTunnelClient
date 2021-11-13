package com.qzero.tunnel.client.data;

import javax.persistence.*;

@Entity
public class ServerProfile {

    @Id
    private String id;
    private String serverIp;
    private String serverName;
    private int entrancePort;

    @Transient
    private ServerPortInfo portInfo;

    public ServerProfile() {
    }

    public ServerProfile(String id, String serverIp, String serverName, int entrancePort, ServerPortInfo portInfo) {
        this.id = id;
        this.serverIp = serverIp;
        this.serverName = serverName;
        this.entrancePort = entrancePort;
        this.portInfo = portInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

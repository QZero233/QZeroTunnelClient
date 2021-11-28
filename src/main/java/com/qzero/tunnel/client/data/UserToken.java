package com.qzero.tunnel.client.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserToken {

    @Id
    private String token;
    private String username;
    private String serverIp;

    public UserToken() {
    }

    public UserToken(String token, String username, String serverIp) {
        this.token = token;
        this.username = username;
        this.serverIp = serverIp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", serverIp='" + serverIp + '\'' +
                '}';
    }
}

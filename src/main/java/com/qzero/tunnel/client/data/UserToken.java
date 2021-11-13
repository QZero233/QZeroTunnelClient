package com.qzero.tunnel.client.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserToken {

    @Id
    private String token;
    private String username;

    public UserToken() {
    }

    public UserToken(String token, String username) {
        this.token = token;
        this.username = username;
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

    @Override
    public String toString() {
        return "UserToken{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

package com.qzero.tunnel.client;

import com.qzero.tunnel.client.data.ServerProfile;
import com.qzero.tunnel.client.data.UserToken;
import org.springframework.stereotype.Component;

@Component
public class GlobalConfigStorage {

    private ServerProfile currentServerProfile;

    private UserToken userToken;

    public ServerProfile getCurrentServerProfile() {
        return currentServerProfile;
    }

    public void setCurrentServerProfile(ServerProfile currentServerProfile) {
        this.currentServerProfile = currentServerProfile;
    }

    public UserToken getUserToken() {
        return userToken;
    }

    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }
}

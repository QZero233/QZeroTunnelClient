package com.qzero.tunnel.client.config;

import com.qzero.tunnel.client.data.ServerProfile;
import com.qzero.tunnel.client.data.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalConfigStorage {

    @Autowired
    private PreferenceConfig preferenceConfig;

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

    public PreferenceConfig getPreferenceConfig() {
        return preferenceConfig;
    }
}

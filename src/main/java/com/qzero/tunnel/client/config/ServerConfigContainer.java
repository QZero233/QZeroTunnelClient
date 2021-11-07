package com.qzero.tunnel.client.config;

import com.qzero.tunnel.client.data.UserToken;

public class ServerConfigContainer {

    private static ServerConfigContainer instance;

    private ServerProfile serverProfile;

    private UserToken userToken;

    private ServerConfigContainer(){

    }

    public static ServerConfigContainer getInstance(){
        if(instance==null)
            instance=new ServerConfigContainer();
        return instance;
    }

    public void setCurrentServerProfile(ServerProfile serverProfile){
        this.serverProfile=serverProfile;
    }

    public ServerProfile getCurrentServerProfile(){
        return serverProfile;
    }

    public UserToken getUserToken() {
        return userToken;
    }

    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }
}

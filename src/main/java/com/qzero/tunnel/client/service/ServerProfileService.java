package com.qzero.tunnel.client.service;

import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.data.ServerPortInfo;
import com.qzero.tunnel.client.data.ServerProfile;
import com.qzero.tunnel.client.data.repository.ServerProfileRepository;
import com.qzero.tunnel.client.utils.NormalHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServerProfileService {

    @Autowired
    private ServerProfileRepository repository;

    private Logger log= LoggerFactory.getLogger(getClass());

    public List<ServerProfile> getAllServerProfile(){
        return repository.findAll();
    }

    /**
     * The function will get all server profile
     * and check one by one if the connection is ok
     * @return
     */
    public List<ServerProfile> getAllAvailableServerProfile(){
        List<ServerProfile> profileList=repository.findAll();
        List<ServerProfile> result=new ArrayList<>();

        for(ServerProfile profile:profileList){
            //Get port info from remote server
            profile=getServerPortAndFillServerProfile(profile);
            if(profile!=null){
                result.add(profile);
            }
        }

        return result;
    }

    /**
     * It will get server port info from remote server
     * and fill it into ServerProfile given
     * @param profile The server profile with serverIp and entrancePort
     * @return If failed to get it, it will return null
     */
    public ServerProfile getServerPortAndFillServerProfile(ServerProfile profile){
        try {
            ServerPortInfo portInfo= JSONObject.parseObject(NormalHttpUtils.doGet("http://"+
                            profile.getServerIp()+":"+profile.getEntrancePort()+"/server/port_info"),
                    ServerPortInfo.class);

            profile.setPortInfo(portInfo);
            return profile;
        }catch (Exception e){
            log.error("Failed to get port info for server "+profile);
            return null;
        }
    }

    public void saveServerProfile(ServerProfile profile){
        repository.save(profile);
    }

}

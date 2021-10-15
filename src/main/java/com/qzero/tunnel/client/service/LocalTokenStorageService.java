package com.qzero.tunnel.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.data.UserToken;
import com.qzero.tunnel.client.utils.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LocalTokenStorageService {

    private Logger log = LoggerFactory.getLogger(getClass());

    private File storageFile;

    private static LocalTokenStorageService instance;

    public static void initialize(File storageFile) throws Exception{
        instance=new LocalTokenStorageService(storageFile);
    }

    public static LocalTokenStorageService getInstance(){
        return instance;
    }

    private LocalTokenStorageService(File storageFile) throws Exception {
        this.storageFile=storageFile;
        if(!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                log.error("Failed to create storage file");
                throw new Exception("Can not find storage file and can not create it neither");
            }
        }
    }

    public List<UserToken> getAllToken() throws Exception{
        synchronized (storageFile){
            String storageString= StreamUtils.readFileIntoString(storageFile);
            if(storageString==null || storageString.equals(""))
                return new ArrayList<>();

            List<UserToken> tokenList= JSON.parseArray(storageString,UserToken.class);
            return tokenList;
        }
    }

    public void saveTokenList(List<UserToken> tokenList) throws Exception{
        synchronized(storageFile){
            if(tokenList==null || tokenList.isEmpty()){
                StreamUtils.writeFile(storageFile,"".getBytes(StandardCharsets.UTF_8));
                return;
            }

            String json= JSONObject.toJSONString(tokenList);
            StreamUtils.writeFile(storageFile,json.getBytes(StandardCharsets.UTF_8));
        }
    }

}

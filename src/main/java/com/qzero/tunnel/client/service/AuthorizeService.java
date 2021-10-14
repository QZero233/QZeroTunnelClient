package com.qzero.tunnel.client.service;

import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.utils.HttpRequestParam;
import com.qzero.tunnel.client.utils.HttpUtils;

public class AuthorizeService {

    /**
     *
     * @return Token
     */
    public static String login(String username,String passwordHash) throws Exception {
        HttpRequestParam param=new HttpRequestParam();
        param.add("username",username);
        param.add("password_hash",passwordHash);

        HttpUtils httpUtils= HttpUtils.getInstance();
        String result=httpUtils.doPost("/auth/login",param);

        ActionResult actionResult=JSONObject.parseObject(result,ActionResult.class);
        if(actionResult.isSucceeded())
            return actionResult.getMessage();
        else
            return null;
    }

    public static boolean register(String username,String passwordHash) throws Exception {
        HttpRequestParam param=new HttpRequestParam();
        param.add("username",username);
        param.add("password_hash",passwordHash);

        HttpUtils httpUtils= HttpUtils.getInstance();
        String result=httpUtils.doPost("/auth/register",param);

        ActionResult actionResult=JSONObject.parseObject(result,ActionResult.class);
        return actionResult.isSucceeded();
    }

}

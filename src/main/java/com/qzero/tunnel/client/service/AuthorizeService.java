package com.qzero.tunnel.client.service;

import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.data.UserToken;
import com.qzero.tunnel.client.data.repository.UserTokenRepository;
import com.qzero.tunnel.client.service.aspect.UseExceptionAdvice;
import com.qzero.tunnel.client.utils.HttpRequestParam;
import com.qzero.tunnel.client.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizeService {

    @Autowired
    private UserTokenRepository tokenRepository;

    /**
     * Login using username and passwordHash
     * @param username username
     * @param passwordHash passwordHash
     * @return The token id of the user is action succeeded, otherwise null
     * @throws Exception Thrown if send http request failed
     */
    public String login(String username,String passwordHash) throws Exception {
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

    @UseExceptionAdvice
    public String register(String username,String passwordHash) throws Exception {
        HttpRequestParam param=new HttpRequestParam();
        param.add("username",username);
        param.add("password_hash",passwordHash);

        HttpUtils httpUtils= HttpUtils.getInstance();
        return httpUtils.doPost("/auth/register",param);
    }

    public boolean checkTokenValidity(String token,String username) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();
        String result= httpUtils.doGet(String.format("/auth/%s/validity?username=%s", token, username));

        ActionResult actionResult=JSONObject.parseObject(result,ActionResult.class);
        return actionResult.isSucceeded();
    }


    public List<UserToken> getAllTokenByServerIp(String ip){
        return tokenRepository.findAllByServerIp(ip);
    }

    public void addUserToken(UserToken userToken){
        tokenRepository.save(userToken);
    }

    public void deleteToken(String token){
        tokenRepository.deleteById(token);
    }

    public void deleteAllTokenByServerIp(String ip){
        tokenRepository.deleteAllByServerIp(ip);
    }

}

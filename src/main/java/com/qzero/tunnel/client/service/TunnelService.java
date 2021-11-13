package com.qzero.tunnel.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.data.TunnelConfig;
import com.qzero.tunnel.client.service.aspect.UseExceptionAdvice;
import com.qzero.tunnel.client.utils.HttpRequestParam;
import com.qzero.tunnel.client.utils.HttpUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TunnelService {

    @UseExceptionAdvice
    public String newTunnel(TunnelConfig tunnelConfig) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        HttpRequestParam param=new HttpRequestParam();
        param.add("crypto_module_name",tunnelConfig.getCryptoModuleName());
        param.add("tunnel_type",tunnelConfig.getTunnelType()+"");

        return httpUtils.doPost("/tunnel/"+tunnelConfig.getTunnelPort(),param);
    }

    @UseExceptionAdvice
    public String updateCryptoModuleName(TunnelConfig tunnelConfig,boolean hot) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        HttpRequestParam param=new HttpRequestParam();
        param.add("crypto_module_name",tunnelConfig.getCryptoModuleName());

        String path="/tunnel/"+tunnelConfig.getTunnelPort()+"/crypto_module_name";
        if(hot)
            path+="/hot";

        return httpUtils.doPut(path,param);
    }

    @UseExceptionAdvice
    public String deleteTunnel(int tunnelPort) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        return httpUtils.doDelete("/tunnel/"+tunnelPort);
    }

    @UseExceptionAdvice
    public String openTunnel(int tunnelPort) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        return httpUtils.doGet(String.format("/tunnel/%d/open", tunnelPort));
    }

    @UseExceptionAdvice
    public String closeTunnel(int tunnelPort) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        return httpUtils.doGet(String.format("/tunnel/%d/close", tunnelPort));
    }

    public TunnelConfig getTunnelConfig(int tunnelPort) throws Exception{
        HttpUtils httpUtils= HttpUtils.getInstance();

        String result=httpUtils.doGet("/tunnel/"+tunnelPort);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded()){
            throw new Exception(actionResult.getMessage());
        }

        String configJson=actionResult.getMessage();
        TunnelConfig config=JSONObject.parseObject(configJson,TunnelConfig.class);
        return config;
    }

    public List<TunnelConfig> getAllTunnelConfig() throws Exception{
        HttpUtils httpUtils= HttpUtils.getInstance();

        String result=httpUtils.doGet("/tunnel/");
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded()){
            throw new Exception(actionResult.getMessage());
        }

        String configJson=actionResult.getMessage();
        return JSON.parseArray(configJson,TunnelConfig.class);
    }

}

package com.qzero.tunnel.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.data.TunnelConfig;
import com.qzero.tunnel.client.utils.HttpRequestParam;
import com.qzero.tunnel.client.utils.HttpUtils;

import java.util.List;

public class TunnelService {

    public static void newTunnel(TunnelConfig tunnelConfig) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        HttpRequestParam param=new HttpRequestParam();
        param.add("local_ip",tunnelConfig.getLocalIp());
        param.add("local_port",tunnelConfig.getLocalPort()+"");

        String result=httpUtils.doPost("/tunnel/"+tunnelConfig.getTunnelPort(),param);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded())
            throw new Exception(actionResult.getMessage());
    }

    public static void updateTunnel(TunnelConfig tunnelConfig) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        HttpRequestParam param=new HttpRequestParam();
        param.add("local_ip",tunnelConfig.getLocalIp());
        param.add("local_port",tunnelConfig.getLocalPort()+"");

        String result=httpUtils.doPut("/tunnel/"+tunnelConfig.getTunnelPort(),param);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded())
            throw new Exception(actionResult.getMessage());
    }

    public static void deleteTunnel(int tunnelPort) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        String result=httpUtils.doDelete("/tunnel/"+tunnelPort);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded()){
            throw new Exception(actionResult.getMessage());
        }
    }

    public static void openTunnel(int tunnelPort) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        String result=httpUtils.doGet(String.format("/tunnel/%d/open", tunnelPort));
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded()){
            throw new Exception(actionResult.getMessage());
        }
    }

    public static void closeTunnel(int tunnelPort) throws Exception {
        HttpUtils httpUtils= HttpUtils.getInstance();

        String result=httpUtils.doGet(String.format("/tunnel/%d/close", tunnelPort));
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded()){
            throw new Exception(actionResult.getMessage());
        }
    }

    public static TunnelConfig getTunnelConfig(int tunnelPort) throws Exception{
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

    public static List<TunnelConfig> getAllTunnelConfig() throws Exception{
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

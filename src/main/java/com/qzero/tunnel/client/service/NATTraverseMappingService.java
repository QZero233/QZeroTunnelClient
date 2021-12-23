package com.qzero.tunnel.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.data.NATTraverseMapping;
import com.qzero.tunnel.client.utils.HttpRequestParam;
import com.qzero.tunnel.client.utils.HttpUtils;

public class NATTraverseMappingService {

    public static void newNATTraverseMapping(NATTraverseMapping natTraverseMapping) throws Exception {
        HttpUtils httpUtils=HttpUtils.getInstance();

        String result=httpUtils.doPost("/nat_traverse_mapping/"+natTraverseMapping.getTunnelPort(),natTraverseMapping);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded())
            throw new Exception(actionResult.getMessage());
    }

    public static void deleteNATTraverseMapping(int tunnelPort) throws Exception {
        HttpUtils httpUtils=HttpUtils.getInstance();

        String result=httpUtils.doDelete("/nat_traverse_mapping/"+tunnelPort);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded())
            throw new Exception(actionResult.getMessage());
    }

    public static void updateNATTraverseMapping(NATTraverseMapping natTraverseMapping) throws Exception{
        HttpUtils httpUtils=HttpUtils.getInstance();

        String path="/nat_traverse_mapping/"+natTraverseMapping.getTunnelPort();

        String result=httpUtils.doPut(path,natTraverseMapping);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded())
            throw new Exception(actionResult.getMessage());
    }

    public static NATTraverseMapping getNATTraverseMapping(int tunnelPort) throws Exception {
        HttpUtils httpUtils=HttpUtils.getInstance();

        String result=httpUtils.doGet("/nat_traverse_mapping/"+tunnelPort);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded())
            throw new Exception(actionResult.getMessage());

        return JSON.parseObject(actionResult.getMessage(),NATTraverseMapping.class);
    }

}

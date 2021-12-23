package com.qzero.tunnel.client.service;

import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.data.NATTraverseMapping;
import com.qzero.tunnel.client.data.VirtualNetworkMapping;
import com.qzero.tunnel.client.utils.HttpRequestParam;
import com.qzero.tunnel.client.utils.HttpUtils;

public class VirtualNetworkMappingService {

    public static void newVirtualNetworkMapping(VirtualNetworkMapping virtualNetworkMapping) throws Exception {
        HttpUtils httpUtils=HttpUtils.getInstance();

        String result=httpUtils.doPost("/virtual_network_mapping/",virtualNetworkMapping);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded())
            throw new Exception(actionResult.getMessage());
    }

    public static void deleteVirtualNetworkMapping(String dstIdentity) throws Exception {
        HttpUtils httpUtils=HttpUtils.getInstance();

        String result=httpUtils.doDelete("/virtual_network_mapping/"+dstIdentity);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded())
            throw new Exception(actionResult.getMessage());
    }

    public static void updateVirtualNetworkMapping(VirtualNetworkMapping virtualNetworkMapping) throws Exception{
        HttpUtils httpUtils=HttpUtils.getInstance();

        String path="/virtual_network_mapping/"+virtualNetworkMapping.getDstIdentity();

        String result=httpUtils.doPut(path,virtualNetworkMapping);
        ActionResult actionResult= JSONObject.parseObject(result,ActionResult.class);

        if(!actionResult.isSucceeded())
            throw new Exception(actionResult.getMessage());
    }

}

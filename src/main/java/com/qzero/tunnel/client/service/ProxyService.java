package com.qzero.tunnel.client.service;

import com.qzero.tunnel.client.exception.ActionFailedException;
import com.qzero.tunnel.client.proxy.ProxyBridgeThread;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProxyService {

    private Map<Integer, ProxyBridgeThread> proxyBridgeThreadMap=new HashMap<>();

    public void openProxyBridge(int localPort,int tunnelPort) throws Exception{
        if(proxyBridgeThreadMap.containsKey(localPort))
            throw new ActionFailedException(String.format("Proxy bridge on port %d already exists", localPort));


        ProxyBridgeThread proxyBridgeThread=new ProxyBridgeThread(localPort,tunnelPort);
        proxyBridgeThread.start();
        proxyBridgeThreadMap.put(localPort,proxyBridgeThread);
    }

    public void closeProxyBridge(int localPort) throws Exception{
        if(!proxyBridgeThreadMap.containsKey(localPort))
            throw new ActionFailedException(String.format("Proxy bridge on port %d is not running", localPort));

        ProxyBridgeThread proxyBridgeThread=proxyBridgeThreadMap.get(localPort);
        proxyBridgeThread.closeBridge();
        proxyBridgeThreadMap.remove(localPort);
    }
}

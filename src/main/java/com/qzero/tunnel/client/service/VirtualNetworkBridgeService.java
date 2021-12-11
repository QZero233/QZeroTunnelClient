package com.qzero.tunnel.client.service;

import com.qzero.tunnel.client.exception.ActionFailedException;
import com.qzero.tunnel.client.virtual.VirtualNetworkBridgeThread;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VirtualNetworkBridgeService {

    private Map<Integer, VirtualNetworkBridgeThread> virtualNetworkBridgeThreadMap =new HashMap<>();

    public void openVirtualNetworkBridge(int localPort,int tunnelPort) throws Exception{
        if(virtualNetworkBridgeThreadMap.containsKey(localPort))
            throw new ActionFailedException(String.format("Virtual network bridge on port %d already exists", localPort));


        VirtualNetworkBridgeThread virtualNetworkBridgeThread=new VirtualNetworkBridgeThread(localPort,tunnelPort);
        virtualNetworkBridgeThread.start();
        virtualNetworkBridgeThreadMap.put(localPort,virtualNetworkBridgeThread);
    }

    public void closeVirtualNetworkBridge(int localPort) throws Exception{
        if(!virtualNetworkBridgeThreadMap.containsKey(localPort))
            throw new ActionFailedException(String.format("Virtual network bridge on port %d is not running", localPort));

        VirtualNetworkBridgeThread virtualNetworkBridgeThread= virtualNetworkBridgeThreadMap.get(localPort);
        virtualNetworkBridgeThread.closeBridge();
        virtualNetworkBridgeThreadMap.remove(localPort);
    }

}

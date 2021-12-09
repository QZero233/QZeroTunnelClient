package com.qzero.tunnel.client.controller;

import com.qzero.tunnel.client.command.CommandMethod;
import com.qzero.tunnel.client.exception.IllegalPortException;
import com.qzero.tunnel.client.virtual.VirtualNetworkBridgeThread;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VirtualNetworkCommand {

    private List<VirtualNetworkBridgeThread> threadList=new ArrayList<>();

    /**
     * start_proxy_bridge localPort tunnelPort
     * @param parts
     * @param commandLine
     */
    @CommandMethod(commandName = "start_virtual_network_bridge",parameterCount = 2)
    private String startVirtualNetworkBridge(String[] parts,String commandLine) throws Exception {
        int localPort;
        try {
            localPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[2]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[2]);
        }

        VirtualNetworkBridgeThread thread=new VirtualNetworkBridgeThread(localPort,tunnelPort);
        thread.start();
        threadList.add(thread);//FIXME for test, remove it

        return "Started successfully";
    }

}

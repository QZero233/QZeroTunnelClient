package com.qzero.tunnel.client.controller;

import com.qzero.tunnel.client.command.CommandMethod;
import com.qzero.tunnel.client.exception.IllegalPortException;
import com.qzero.tunnel.client.service.VirtualNetworkBridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class VirtualNetworkCommand {

    @Autowired
    private VirtualNetworkBridgeService service;

    /**
     * start_virtual_network_bridge localPort tunnelPort
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

        service.openVirtualNetworkBridge(localPort,tunnelPort);

        return "Started successfully";
    }

    /**
     * stop_virtual_network_bridge localPort
     * @param parts
     * @param commandLine
     */
    @CommandMethod(commandName = "stop_virtual_network_bridge",parameterCount = 1)
    private String stopVirtualNetworkBridge(String[] parts,String commandLine) throws Exception {
        int localPort;
        try {
            localPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        service.closeVirtualNetworkBridge(localPort);
        return "Stopped successfully";
    }

}

package com.qzero.tunnel.client.controller;

import com.qzero.tunnel.client.command.CommandMethod;
import com.qzero.tunnel.client.exception.IllegalPortException;
import com.qzero.tunnel.client.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProxyCommand {

    @Autowired
    private ProxyService service;

    /**
     * start_proxy_bridge localPort tunnelPort
     * @param parts
     * @param commandLine
     */
    @CommandMethod(commandName = "start_proxy_bridge",parameterCount = 2)
    private String startProxyBridge(String[] parts,String commandLine) throws Exception {
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

        service.openProxyBridge(localPort,tunnelPort);
        return "Started successfully";
    }

    /**
     * stop_proxy_bridge localPort
     * @param parts
     * @param commandLine
     */
    @CommandMethod(commandName = "stop_proxy_bridge",parameterCount = 1)
    private String stopProxyBridge(String[] parts,String commandLine) throws Exception {
        int localPort;
        try {
            localPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        service.closeProxyBridge(localPort);
        return "Stopped successfully";
    }

}

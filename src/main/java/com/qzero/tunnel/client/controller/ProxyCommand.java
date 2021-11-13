package com.qzero.tunnel.client.controller;

import com.qzero.tunnel.client.command.CommandMethod;
import com.qzero.tunnel.client.exception.IllegalPortException;
import com.qzero.tunnel.client.proxy.ProxyBridgeThread;
import org.springframework.stereotype.Controller;

@Controller
public class ProxyCommand {

    /**
     * start_proxy_bridge localPort tunnelPort
     * @param parts
     * @param commandLine
     */
    @CommandMethod(commandName = "start_proxy_bridge",parameterCount = 2)
    private String startProxyBridge(String[] parts,String commandLine) throws IllegalPortException {
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

        new ProxyBridgeThread(localPort,tunnelPort).start();
        return "Started successfully";
    }

}

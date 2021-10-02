package com.qzero.tunnel.client.command;

import com.qzero.tunnel.client.data.TunnelConfig;
import com.qzero.tunnel.client.exception.IllegalPortException;
import com.qzero.tunnel.client.service.TunnelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TunnelCommand {

    private Logger log= LoggerFactory.getLogger(getClass());

    /**
     * new_tunnel tunnelPort localIp localPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "new_tunnel",parameterCount = 3)
    private String newTunnel(String[] parts,String commandLine){
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        int localPort;
        try {
            localPort=Integer.parseInt(parts[3]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[3]);
        }

        TunnelConfig tunnelConfig=new TunnelConfig(tunnelPort,parts[2],localPort);
        try {
            TunnelService.newTunnel(tunnelConfig);
            return "New tunnel has been created";
        }catch (Exception e){
            log.error("Failed to new tunnel port for "+tunnelConfig,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

    /**
     * update_tunnel tunnelPort localIp localPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "update_tunnel",parameterCount = 3)
    private String updateTunnel(String[] parts,String commandLine){
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        int localPort;
        try {
            localPort=Integer.parseInt(parts[3]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[3]);
        }

        TunnelConfig tunnelConfig=new TunnelConfig(tunnelPort,parts[2],localPort);
        try {
            TunnelService.updateTunnel(tunnelConfig);
            return String.format("Tunnel config on %d has been updated", tunnelPort);
        }catch (Exception e){
            log.error("Failed to update tunnel config for "+tunnelConfig,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

    /**
     * delete_tunnel tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "delete_tunnel",parameterCount = 1)
    private String deleteTunnel(String[] parts,String commandLine){
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        try {
            TunnelService.deleteTunnel(tunnelPort);
            return String.format("Tunnel on %d has been deleted", tunnelPort);
        }catch (Exception e){
            log.error("Failed to delete tunnel on "+tunnelPort,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

    /**
     * open_tunnel tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "open_tunnel",parameterCount = 1)
    private String openTunnel(String[] parts,String commandLine){
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        try {
            TunnelService.openTunnel(tunnelPort);
            return String.format("Tunnel on %d has opened", tunnelPort);
        }catch (Exception e){
            log.error("Failed to open tunnel on "+tunnelPort,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

    /**
     * close_tunnel tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "close_tunnel",parameterCount = 1)
    private String closeTunnel(String[] parts,String commandLine){
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        try {
            TunnelService.closeTunnel(tunnelPort);
            return String.format("Tunnel on %d has been closed", tunnelPort);
        }catch (Exception e){
            log.error("Failed to close tunnel on "+tunnelPort,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

}

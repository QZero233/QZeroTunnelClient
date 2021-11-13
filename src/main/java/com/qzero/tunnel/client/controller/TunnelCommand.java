package com.qzero.tunnel.client.controller;

import com.qzero.tunnel.client.command.CommandMethod;
import com.qzero.tunnel.client.data.TunnelConfig;
import com.qzero.tunnel.client.exception.IllegalPortException;
import com.qzero.tunnel.client.service.TunnelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TunnelCommand {

    private Logger log= LoggerFactory.getLogger(getClass());

    private int convertTunnelType(String tunnelTypeInString) {
        switch (tunnelTypeInString.toLowerCase()){
            case "nattraverse":
                return TunnelConfig.TYPE_NAT_TRAVERSE;
            case "proxy":
                return TunnelConfig.TYPE_PROXY;
            default:
                throw new IllegalArgumentException("No tunnel type "+tunnelTypeInString);
        }
    }

    /**
     * new_tunnel tunnelPort cryptoModuleName tunnelTypeInString
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

        int tunnelType = convertTunnelType(parts[3]);

        TunnelConfig tunnelConfig=new TunnelConfig(tunnelPort,parts[2],tunnelType);
        try {
            TunnelService.newTunnel(tunnelConfig);
            return "New tunnel has been created";
        }catch (Exception e){
            log.error("Failed to new tunnel port for "+tunnelConfig,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

    /**
     * update_tunnel_crypto tunnelPort cryptoModuleName isHot
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "update_tunnel_crypto",parameterCount = 3)
    private String updateTunnelCrypto(String[] parts,String commandLine){
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }


        TunnelConfig tunnelConfig=new TunnelConfig(tunnelPort,parts[2],0);
        boolean isHot=Boolean.getBoolean(parts[3]);

        try {
            TunnelService.updateCryptoModuleName(tunnelConfig,isHot);
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

    /**
     * get_tunnel_config tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "get_tunnel_config",parameterCount = 1)
    private String getTunnelConfig(String[] parts,String commandLine){
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        try {
            TunnelConfig config=TunnelService.getTunnelConfig(tunnelPort);
            return config.toString();
        }catch (Exception e){
            log.error("Failed to get tunnel config on "+tunnelPort,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

    /**
     * get_all_tunnel_config
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "get_all_tunnel_config",parameterCount = 0)
    private String getAllTunnelConfig(String[] parts,String commandLine){
        try {
            List<TunnelConfig> configList=TunnelService.getAllTunnelConfig();
            return configList.toString();
        }catch (Exception e){
            log.error("Failed to get all tunnel config ",e);
            return "Failed, reason: "+e.getMessage();
        }
    }

}

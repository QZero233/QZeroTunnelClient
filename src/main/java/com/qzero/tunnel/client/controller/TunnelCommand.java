package com.qzero.tunnel.client.controller;

import com.qzero.tunnel.client.command.CommandMethod;
import com.qzero.tunnel.client.data.TunnelConfig;
import com.qzero.tunnel.client.exception.IllegalPortException;
import com.qzero.tunnel.client.service.TunnelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TunnelCommand {

    private Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private TunnelService tunnelService;

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
    private String newTunnel(String[] parts,String commandLine) throws Exception {
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        int tunnelType = convertTunnelType(parts[3]);

        TunnelConfig tunnelConfig=new TunnelConfig(tunnelPort,parts[2],tunnelType);

        tunnelService.newTunnel(tunnelConfig);
        return "New tunnel has been created";
    }

    /**
     * update_tunnel_crypto tunnelPort cryptoModuleName isHot
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "update_tunnel_crypto",parameterCount = 3)
    private String updateTunnelCrypto(String[] parts,String commandLine) throws Exception {
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }


        TunnelConfig tunnelConfig=new TunnelConfig(tunnelPort,parts[2],0);
        boolean isHot=Boolean.getBoolean(parts[3]);


        tunnelService.updateCryptoModuleName(tunnelConfig,isHot);
        return String.format("Tunnel config on %d has been updated", tunnelPort);
    }

    /**
     * delete_tunnel tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "delete_tunnel",parameterCount = 1)
    private String deleteTunnel(String[] parts,String commandLine) throws Exception{
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        tunnelService.deleteTunnel(tunnelPort);
        return String.format("Tunnel on %d has been deleted", tunnelPort);
    }

    /**
     * open_tunnel tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "open_tunnel",parameterCount = 1)
    private String openTunnel(String[] parts,String commandLine) throws Exception {
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }


        tunnelService.openTunnel(tunnelPort);
        return String.format("Tunnel on %d has opened", tunnelPort);
    }

    /**
     * close_tunnel tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "close_tunnel",parameterCount = 1)
    private String closeTunnel(String[] parts,String commandLine) throws Exception {
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }


        tunnelService.closeTunnel(tunnelPort);
        return String.format("Tunnel on %d has been closed", tunnelPort);
    }

    /**
     * get_tunnel_config tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "get_tunnel_config",parameterCount = 1)
    private String getTunnelConfig(String[] parts,String commandLine) throws Exception {
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }


        TunnelConfig config=tunnelService.getTunnelConfig(tunnelPort);
        return config.toString();
    }

    /**
     * get_all_tunnel_config
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "get_all_tunnel_config")
    private String getAllTunnelConfig(String[] parts,String commandLine) throws Exception {
        List<TunnelConfig> configList=tunnelService.getAllTunnelConfig();
        return configList.toString();
    }

}

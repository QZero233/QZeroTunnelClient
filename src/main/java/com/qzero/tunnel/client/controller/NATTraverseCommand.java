package com.qzero.tunnel.client.controller;

import com.qzero.tunnel.client.command.CommandMethod;
import com.qzero.tunnel.client.data.NATTraverseMapping;
import com.qzero.tunnel.client.exception.IllegalPortException;
import com.qzero.tunnel.client.remind.RemindThread;
import com.qzero.tunnel.client.service.NATTraverseMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class NATTraverseCommand {

    private Logger log= LoggerFactory.getLogger(getClass());

    /**
     * new_nat_traverse_mapping tunnelPort localIp localPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "new_nat_traverse_mapping",parameterCount = 3)
    private String newNATTraverseMapping(String[] parts,String commandLine) throws Exception {
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

        NATTraverseMapping natTraverseMapping=new NATTraverseMapping(tunnelPort,parts[2],localPort);
        NATTraverseMappingService.newNATTraverseMapping(natTraverseMapping);
        return "New NATTraverseMapping has been created";
    }

    /**
     * update_nat_traverse_mapping tunnelPort localIp localPort isHot
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "update_nat_traverse_mapping",parameterCount = 4)
    private String updateNATTraverseMapping(String[] parts,String commandLine) throws Exception {
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

        NATTraverseMapping natTraverseMapping=new NATTraverseMapping(tunnelPort,parts[2],localPort);
        boolean isHot=Boolean.getBoolean(parts[4]);


        NATTraverseMappingService.updateNATTraverseMapping(natTraverseMapping,isHot);
        return String.format("NATTraverseMapping on %d has been updated", tunnelPort);
    }

    /**
     * delete_nat_traverse_mapping tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "delete_nat_traverse_mapping",parameterCount = 1)
    private String deleteNATTraverseMapping(String[] parts,String commandLine) throws Exception {
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }


        NATTraverseMappingService.deleteNATTraverseMapping(tunnelPort);
        return String.format("NAT traverse mapping on %d has been deleted", tunnelPort);
    }

    /**
     * get_nat_traverse_mapping tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "get_nat_traverse_mapping",parameterCount = 1)
    private String getNATTraverseMapping(String[] parts,String commandLine) throws Exception {
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }


        NATTraverseMapping natTraverseMapping=NATTraverseMappingService.getNATTraverseMapping(tunnelPort);
        return natTraverseMapping.toString();
    }


    /**
     * reconnect_to_remind_server
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "reconnect_to_remind_server")
    private String reconnect(String[] parts,String commandLine) throws Exception{
        RemindThread.renewInstance();
        RemindThread.getInstance().start();

        return "Reconnecting to remind server";
    }

}

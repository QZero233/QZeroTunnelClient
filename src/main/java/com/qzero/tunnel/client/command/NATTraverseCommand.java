package com.qzero.tunnel.client.command;

import com.qzero.tunnel.client.data.NATTraverseMapping;
import com.qzero.tunnel.client.exception.IllegalPortException;
import com.qzero.tunnel.client.service.NATTraverseMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NATTraverseCommand {

    private Logger log= LoggerFactory.getLogger(getClass());

    /**
     * new_nat_traverse_mapping tunnelPort localIp localPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "new_nat_traverse_mapping",parameterCount = 3)
    private String newNATTraverseMapping(String[] parts,String commandLine){
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
        try {
            NATTraverseMappingService.newNATTraverseMapping(natTraverseMapping);
            return "New NATTraverseMapping has been created";
        }catch (Exception e){
            log.error("Failed to new NATTraverseMapping for "+natTraverseMapping,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

    /**
     * update_nat_traverse_mapping tunnelPort localIp localPort isHot
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "update_nat_traverse_mapping",parameterCount = 4)
    private String updateNATTraverseMapping(String[] parts,String commandLine){
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

        try {
            NATTraverseMappingService.updateNATTraverseMapping(natTraverseMapping,isHot);
            return String.format("NATTraverseMapping on %d has been updated", tunnelPort);
        }catch (Exception e){
            log.error("Failed to update NATTraverseMapping for "+natTraverseMapping,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

    /**
     * delete_nat_traverse_mapping tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "delete_nat_traverse_mapping",parameterCount = 1)
    private String deleteNATTraverseMapping(String[] parts,String commandLine){
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        try {
            NATTraverseMappingService.deleteNATTraverseMapping(tunnelPort);
            return String.format("NAT traverse mapping on %d has been deleted", tunnelPort);
        }catch (Exception e){
            log.error("Failed to delete NAT traverse mapping on "+tunnelPort,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

    /**
     * get_nat_traverse_mapping tunnelPort
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "get_nat_traverse_mapping",parameterCount = 1)
    private String getNATTraverseMapping(String[] parts,String commandLine){
        int tunnelPort;
        try {
            tunnelPort=Integer.parseInt(parts[1]);
        }catch (NumberFormatException e){
            throw new IllegalPortException(parts[1]);
        }

        try {
            NATTraverseMapping natTraverseMapping=NATTraverseMappingService.getNATTraverseMapping(tunnelPort);
            return natTraverseMapping.toString();
        }catch (Exception e){
            log.error("Failed to get NAT traverse mapping on "+tunnelPort,e);
            return "Failed, reason: "+e.getMessage();
        }
    }

}

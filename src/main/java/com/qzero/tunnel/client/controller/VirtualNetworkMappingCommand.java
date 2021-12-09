package com.qzero.tunnel.client.controller;

import com.qzero.tunnel.client.command.CommandMethod;
import com.qzero.tunnel.client.data.VirtualNetworkMapping;
import com.qzero.tunnel.client.service.VirtualNetworkMappingService;
import org.springframework.stereotype.Controller;

@Controller
public class VirtualNetworkMappingCommand {

    /**
     * new_virtual_network_mapping dstIdentity dstUsername
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "new_virtual_network_mapping",parameterCount = 2)
    private String newVirtualNetworkMapping(String[] parts,String commandLine) throws Exception {
        VirtualNetworkMapping mapping=new VirtualNetworkMapping(parts[1],parts[2]);
        VirtualNetworkMappingService.newVirtualNetworkMapping(mapping);
        return "New VirtualNetworkMapping has been created";
    }

    /**
     * update_virtual_network_mapping dstIdentity dstUsername
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "update_virtual_network_mapping",parameterCount = 2)
    private String updateVirtualNetworkMapping(String[] parts,String commandLine) throws Exception {
        VirtualNetworkMapping mapping=new VirtualNetworkMapping(parts[1],parts[2]);
        VirtualNetworkMappingService.newVirtualNetworkMapping(mapping);
        return String.format("VirtualNetworkMapping for %s has been updated", parts[1]);
    }

    /**
     * delete_virtual_network_mapping dstIdentity
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "delete_virtual_network_mapping",parameterCount = 1)
    private String deleteVirtualNetworkMapping(String[] parts,String commandLine) throws Exception {
        VirtualNetworkMappingService.deleteVirtualNetworkMapping(parts[1]);
        return String.format("VirtualNetworkMapping for %s has been deleted", parts[1]);
    }

}

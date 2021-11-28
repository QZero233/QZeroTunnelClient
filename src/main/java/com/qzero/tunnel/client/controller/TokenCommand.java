package com.qzero.tunnel.client.controller;

import com.qzero.tunnel.client.GlobalConfigStorage;
import com.qzero.tunnel.client.command.CommandMethod;
import com.qzero.tunnel.client.data.UserToken;
import com.qzero.tunnel.client.service.AuthorizeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TokenCommand {

    private Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthorizeService authorizeService;

    @Autowired
    private GlobalConfigStorage configStorage;

    /**
     * get_all_token
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "get_all_token")
    private String getAllToken(String[] parts,String commandLine){
        List<UserToken> tokenList;
        try {
            tokenList=authorizeService.getAllTokenByServerIp(configStorage.getCurrentServerProfile().getServerIp());
        }catch (Exception e){
            log.error("Failed to get all token",e);
            return "Failed to get all token";
        }

        if(tokenList==null || tokenList.isEmpty()){
            return "Token storage is empty";
        }

        String result="";
        int i=0;
        for(UserToken token:tokenList){
            result+=String.format("%d)%s %s\n", i,token.getUsername(),token.getToken());
            i++;
        }

        return result;
    }

    /**
     * delete_token tokenId
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "delete_token", parameterCount = 1)
    private String deleteToken(String[] parts,String commandLine){
        authorizeService.deleteToken(parts[1]);
        return "Deleted token with tokenId  "+parts[1];
    }

    /**
     * delete_all_token
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "delete_all_token")
    private String deleteAllToken(String[] parts,String commandLine){

        authorizeService.deleteAllTokenByServerIp(configStorage.getCurrentServerProfile().getServerIp());
        return "Deleted all token";
    }

}

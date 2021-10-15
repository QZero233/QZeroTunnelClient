package com.qzero.tunnel.client.command;

import com.qzero.tunnel.client.data.UserToken;
import com.qzero.tunnel.client.service.LocalTokenStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TokenCommand {

    private Logger log= LoggerFactory.getLogger(getClass());

    private LocalTokenStorageService storageService=LocalTokenStorageService.getInstance();

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
            tokenList=storageService.getAllToken();
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
     * delete_token tokenIndex
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "delete_token", parameterCount = 1)
    private String deleteToken(String[] parts,String commandLine){
        String tokenIndexString=parts[1];
        int tokenIndex;

        try {
            tokenIndex=Integer.parseInt(tokenIndexString);
        }catch (Exception e){
            return "Illegal input of token index: not a number";
        }

        try {
            List<UserToken> tokenList=storageService.getAllToken();
            if(tokenIndex<0 || tokenIndex>tokenList.size()-1){
                return "Illegal input of token index: out of bound";
            }

            tokenList.remove(tokenIndex);
            storageService.saveTokenList(tokenList);
        }catch (Exception e){
            log.error("Failed to delete token at index "+tokenIndex,e);
            return "Failed to delete token at index "+tokenIndex;
        }

        return "Deleted token at index "+tokenIndex;
    }

    /**
     * delete_all_token
     * @param parts
     * @param commandLine
     * @return
     */
    @CommandMethod(commandName = "delete_all_token")
    private String deleteAllToken(String[] parts,String commandLine){
        try {
            storageService.saveTokenList(new ArrayList<>());
            return "Deleted all token";
        }catch (Exception e){
            log.error("Failed to delete all token",e);
            return "Failed to delete all token";
        }
    }

}

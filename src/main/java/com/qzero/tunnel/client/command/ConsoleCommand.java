package com.qzero.tunnel.client.command;

public interface ConsoleCommand {

    int getCommandParameterCount();

    /**
     *
     * @param commandParts index 0 is the command name, parameters begin with index 1
     * @param fullCommand
     * @return
     */
    String execute(String[] commandParts, String fullCommand);

}

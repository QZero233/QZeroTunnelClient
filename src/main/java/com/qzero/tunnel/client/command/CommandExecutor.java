package com.qzero.tunnel.client.command;

import com.qzero.tunnel.client.SpringUtil;
import com.qzero.tunnel.client.exception.ActionFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@DependsOn("spring_util")
public class CommandExecutor {

    private Logger log= LoggerFactory.getLogger(getClass());

    private Map<String, ConsoleCommand> commandMap=new HashMap<>();

    public CommandExecutor() throws Exception {
        //Get all controller and load command functions within them
        ApplicationContext applicationContext= SpringUtil.getApplicationContextStatic();
        Map<String,Object> beans=applicationContext.getBeansWithAnnotation(Controller.class);

        for(Map.Entry<String,Object> entry:beans.entrySet()){
            Object obj=entry.getValue();
            if(obj==null)
                continue;

            try {
                loadCommands(obj);
            }catch (Exception e){
                log.error("Failed to load commands for class "+obj.getClass().getName());
            }
        }
    }

    /**
     * Get all command functions from an object and load them
     * @param instance The object
     */
    private void loadCommands(Object instance) {
        Class cls=instance.getClass();

        Method[] methods=cls.getDeclaredMethods();
        for(Method method:methods){
            CommandMethod commandMethodAnnotation=method.getDeclaredAnnotation(CommandMethod.class);
            if(commandMethodAnnotation==null)
                continue;

            String commandName=commandMethodAnnotation.commandName();
            if(commandName==null)
                throw new IllegalArgumentException(String.format("Command name for class %s can not be empty", cls.getName()));

            if(commandMap.containsKey(commandName))
                throw new IllegalArgumentException(String.format("Command %s has more than one implements", commandName));

            Type[] parameters=method.getParameterTypes();
            if(parameters.length!=2)
                throw new IllegalArgumentException(String.format("Command method %s for command %s does not have 2 parameters(actually it's %d)",
                        method.getName(),commandName,parameters.length));

            if(!(parameters[0].equals(String[].class) &&
            parameters[1].equals(String.class)))
                throw new IllegalArgumentException(String.format("Command method %s for command %s does not have matched parameter types",
                        method.getName(),commandName));

            if(!method.getReturnType().equals(String.class))
                throw new IllegalArgumentException(String.format("Command method %s for command %s does not take String as return value",
                        method.getName(),commandName));


            commandMap.put(commandName, new ConsoleCommand() {
                @Override
                public int getCommandParameterCount() {
                    return commandMethodAnnotation.parameterCount();
                }

                @Override
                public String execute(String[] commandParts, String fullCommand) throws InvocationTargetException, IllegalAccessException {
                    method.setAccessible(true);
                    return (String) method.invoke(instance,commandParts,fullCommand);
                }
            });
        }
    }

    /**
     * Split a full command line into parts
     * The part with quotation marks will be regarded as a whole part
     * @param commandLine The full command line
     * @return The split parts
     */
    private String[] splitCommand(String commandLine){
        byte[] buf=commandLine.getBytes();
        List<String> commandParts=new ArrayList<>();


        ByteArrayOutputStream current=new ByteArrayOutputStream();
        //ByteOutputStream current=new ByteOutputStream();
        boolean whole=false;
        boolean escape=false;
        for(byte b:buf) {
            if (escape) {
                current.write(b);
                escape = false;
                continue;
            }

            if (b == '\\') {
                escape = true;
                continue;
            }

            if (b == '\"') {
                whole = !whole;
                continue;
            }

            if (b == ' ') {
                if (whole) {
                    current.write(b);
                } else {
                    commandParts.add(new String(current.toByteArray(),0, current.size()));
                    current = new ByteArrayOutputStream();
                }
                continue;
            }

            current.write(b);
        }

        commandParts.add(new String(current.toByteArray(),0, current.size()));

        return commandParts.toArray(new String[]{});
    }

    /**
     * Execute a command line and get the result
     * @param commandLine The command line
     * @return The result
     */
    public String executeCommand(String commandLine){
        String[] parts=splitCommand(commandLine);
        String commandName=parts[0];
        if(!commandMap.containsKey(commandName))
            return "Unknown command called "+commandName;

        ConsoleCommand consoleCommand=commandMap.get(commandName);

        int parameterCount=consoleCommand.getCommandParameterCount();
        if(parts.length-1<parameterCount)
            return String.format("Command %s need as least %d parameters, but there are only %d", commandName,
                    parameterCount,parts.length-1);


        //Process action failed and invoke method failed
        //Illegal parameter will be processed in controller
        try {
            return consoleCommand.execute(parts,commandLine);
        }catch (InvocationTargetException invocationTargetException){
            //Caused by method it called

            Throwable target=invocationTargetException.getTargetException();
            if(target instanceof ActionFailedException){
                //Action failed
                ActionFailedException actionFailedException= (ActionFailedException) target;
                log.error("Failed to execute command method "+actionFailedException.getActionMethodName(),actionFailedException);
                return "Failed, reason: "+actionFailedException.getReason();
            }else{
                //Unknown exception
                log.error(String.format("Failed to execute command %s for unknown reason", commandName),target);
                return "Unknown error, please see log for detail";
            }

        }catch (IllegalAccessException e){
            //Failed to invoke method
            log.error(String.format("Failed to invoke command method for command %s",commandName),e);
            return "Failed to invoke command method, please see logs for detail";
        }catch (Exception e){
            //Other unknown exception
            log.error(String.format("Failed to execute command %s for unknown reason", commandName),e);
            return "Unknown error, please see log for detail";
        }

    }

}

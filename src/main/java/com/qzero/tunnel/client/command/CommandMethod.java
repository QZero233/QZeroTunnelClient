package com.qzero.tunnel.client.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMethod {

    String commandName();
    int parameterCount() default 0;

}

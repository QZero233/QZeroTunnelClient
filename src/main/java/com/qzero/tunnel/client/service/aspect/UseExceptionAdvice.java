package com.qzero.tunnel.client.service.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UseExceptionAdvice {

    boolean value() default true;

}

package com.qzero.tunnel.client.service.aspect;

import com.alibaba.fastjson.JSONObject;
import com.qzero.tunnel.client.exception.ActionFailedException;
import com.qzero.tunnel.client.service.ActionResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ServiceExceptionAdviceAspect {

    private Logger log= LoggerFactory.getLogger(getClass());

    @Pointcut("execution(String com.qzero.tunnel.client.service.*.*(..))")
    public void serviceMethod(){

    }

    @Around("serviceMethod()")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method=methodSignature.getMethod();
        UseExceptionAdvice annotation=method.getAnnotation(UseExceptionAdvice.class);
        if(annotation==null || !annotation.value())
            return proceedingJoinPoint.proceed();

        String httpResult;
        ActionResult actionResult;

        try {
            httpResult= (String) proceedingJoinPoint.proceed();
            actionResult= JSONObject.parseObject(httpResult,ActionResult.class);
        }catch (Exception e){
            log.error("Failed to convert service result into ActionResult object",e);
            throw new Exception("Failed to convert service result into ActionResult object, please see log for detailed message");
        }

        if(actionResult.isSucceeded())
            return httpResult;
        else
            throw new ActionFailedException(method.getName(),actionResult.getMessage());
    }

}

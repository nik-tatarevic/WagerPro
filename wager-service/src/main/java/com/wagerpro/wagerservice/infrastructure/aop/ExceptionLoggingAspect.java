package com.wagerpro.wagerservice.infrastructure.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    @AfterThrowing(
            pointcut = "within(@org.springframework.stereotype.Service *)",
            throwing = "ex")
    public void logException(JoinPoint jp, Exception ex) {

        log.error(
                "Exception in {}.{}(): {}",
                jp.getSignature().getDeclaringTypeName(),
                jp.getSignature().getName(),
                ex.getMessage(),
                ex
        );
    }
    
}

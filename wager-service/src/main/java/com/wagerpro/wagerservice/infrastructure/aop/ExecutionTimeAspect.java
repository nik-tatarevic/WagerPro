package com.wagerpro.wagerservice.infrastructure.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("""
    		within(@org.springframework.stereotype.Service *)
    		""")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        log.info("{} executed in {} ms",
                joinPoint.getSignature().toShortString(),
                duration);

        return result;
    }
    
    @Around("""
    		within(@org.springframework.stereotype.Service *)
    		""")
    public Object measureTimeAbove500ms(ProceedingJoinPoint joinPoint) throws Throwable {
    	long start = System.currentTimeMillis();
        
        try {
            return joinPoint.proceed(); 
        } finally {
            long duration = System.currentTimeMillis() - start;
            
            if (duration > 500) {
                log.warn("Slow method detected! {} executed in {} ms",
                        joinPoint.getSignature().toShortString(),
                        duration);
            }
        }
    }
    
}

package com.wagerpro.wagerservice.infrastructure.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
	
	@Pointcut("""
		    within(@org.springframework.web.bind.annotation.RestController *) ||
		    within(@org.springframework.stereotype.Service *)
		""")
	public void applicationLayers() {}
	
	@Before("applicationLayers()")
	public void logMethodStart(JoinPoint joinPoint) {

        log.info("Entering {}.{}()",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

	@AfterReturning("applicationLayers()")
	public void logMethodEnd(JoinPoint joinPoint) {
	
	    log.info("Finished {}.{}()",
	            joinPoint.getSignature().getDeclaringTypeName(),
	            joinPoint.getSignature().getName());
	}

}

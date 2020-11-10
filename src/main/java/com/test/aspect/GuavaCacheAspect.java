package com.test.aspect;

import com.test.annotation.GuavaCache;
import com.test.cache.GuavaCacheManager;
import com.test.util.KeyGenerator;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GuavaCacheAspect {

    @Autowired
    @Qualifier(value = "guavaCacheManager")
    private GuavaCacheManager guavaCacheManager;

    @Autowired
    private KeyGenerator keyGenerator;

    @Pointcut("@annotation(com.test.annotation.GuavaCache)")
    public void guavaCachePoint(){

    }

    @Around(value = "guavaCachePoint() && @annotation(guavaCache)")
    public Object doAround(ProceedingJoinPoint point, GuavaCache guavaCache) throws Throwable{
        log.debug("GuavaCacheAspect with param={}", guavaCache);
        Object object = null;
        String key = null;
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            String methodName = signature.getName();
            Class<?> classTarget = point.getTarget().getClass();
            Method method = classTarget.getMethod(methodName, signature.getParameterTypes());
            key = keyGenerator.generatorKey(method, guavaCache.cacheKey(), point.getArgs());
            if(StringUtils.isNotEmpty(key)){
                object = guavaCacheManager.getCache(key);
                if(object != null){
                    log.debug("GuavaCacheAspect get cache with cacheKey = {}", key);
                    return object;
                }
            }
        } catch (Exception e){
            log.error("GuavaCacheAspect doAround error with exception = {}", e);
        }
        object = point.proceed();
        if(object != null && StringUtils.isNotEmpty(key)){
            guavaCacheManager.putCache(key,object);
            log.info("GuavaCacheAspect set cache with cacheKey = {}", key);
        }
        return object;
    }
}

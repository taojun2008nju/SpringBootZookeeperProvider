package com.test.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.test.cache.GuavaCacheManager;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020/6/27 16:18:00
 * @description TODO
 */
@Slf4j
@Component
public class GuavaCacheConfig {

    @Autowired
    private GuavaProperties guavaProperties;

    @Bean(name = "cacheBuilder")
    public CacheBuilder<Object,Object> cacheBuilder(){
        long maximumSize = guavaProperties.getMaximumSize();
        long expireAfterWrite = guavaProperties.getExpireAfterWriteDuration();
        long expireAfterAccess = guavaProperties.getExpireAfterAccessDuration();
        long refreshDuration = guavaProperties.getRefreshDuration();

        if(maximumSize <= 0){
            maximumSize = 1024;
        }
        if(expireAfterAccess <= 0){
            expireAfterAccess = 3600;
        }
        if(expireAfterWrite <= 0){
            expireAfterWrite = 3600;
        }
        if(refreshDuration <= 0){
            refreshDuration = 1800;
        }

        return CacheBuilder.newBuilder().maximumSize(maximumSize)
            .expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS)
            .refreshAfterWrite(refreshDuration,TimeUnit.SECONDS);
    }

    @Bean(name = "guavaCacheManager")
    public GuavaCacheManager guavaCacheManager(@Qualifier("cacheBuilder")CacheBuilder cacheBuilder){
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        cacheManager.setCacheBuilder(cacheBuilder);
        cacheManager.initCache();
        return cacheManager;
    }
}

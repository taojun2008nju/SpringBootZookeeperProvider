package com.test.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @date 2020/6/27 15:48:00
 * @description guava缓存
 */
@Data
@Configuration
public class GuavaProperties {

    @Value("${guava.cache.maximumSize:500}")
    private long maximumSize;

    @Value("${guava.cache.maximumWeight:100}")
    private long maximumWeight;

    @Value("${guava.cache.expireAfterAccessDuration:-1}")
    private long expireAfterAccessDuration;

    @Value("${guava.cache.expireAfterWriteDuration:-1}")
    private long expireAfterWriteDuration;

    @Value("${guava.cache.refreshDuration:100}")
    private long refreshDuration;

    @Value("${guava.cache.initialCapacity:50}")
    private int initialCapacity;

    @Value("${guava.cache.concurrencyLevel:8}")
    private int concurrencyLevel;
}

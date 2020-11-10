package com.test.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.test.util.DateTimeUtils;
import io.lettuce.core.RedisException;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 * 
 * @author shenweirong@ysten.com
 * @date 2018年10月23日 下午8:47:15
 */
@Configuration
@EnableCaching
@Slf4j
public class RedisCacheConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.default_cache_time:60}")
    private long default_cache_time;

    @Autowired
    private RedisProperties redisProperties;

    @Value("${spring.redis.lettuce.pool.share-native-connection:true}")
    private boolean shareNativeConnection;
    
    
    // 缓存管理器
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory);
        @SuppressWarnings("serial")
        Set<String> cacheNames = new HashSet<String>() {
            {
                add("codeNameCache");
            }
        };
        builder.initialCacheNames(cacheNames);
        RedisCacheConfiguration defaultCacheConfig = this.redisCacheConfiguration();
        if(default_cache_time>0){
            defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofMinutes(default_cache_time));
        }
        RedisCacheManager rcm  =  builder.cacheDefaults(defaultCacheConfig).transactionAware().build();
        return rcm;
    }

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuffer sb = new StringBuffer();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJacksonSerializer();
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        if (default_cache_time > 0) {
            configuration.entryTtl(Duration.ofMinutes(default_cache_time));
        }
        return configuration;
    }

    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJacksonSerializer();
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);// key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer<Object> getJacksonSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
            new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
        om.enableDefaultTyping(DefaultTyping.NON_FINAL);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new Jdk8Module());
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeUtils.YYYY_MM_DD));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeUtils.HH_MM_SS));
        javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeUtils.YYYY_MM_DD));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeUtils.HH_MM_SS));
        om.registerModule(new SimpleModule());
        om.registerModule(javaTimeModule);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = null;
        Cluster cluster = redisProperties.getCluster();
        if (cluster != null && CollectionUtils.isNotEmpty(cluster.getNodes())) {
            RedisClusterConfiguration redisClusterConfiguration =
                new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
            if (StringUtils.isNotEmpty(redisProperties.getPassword())) {
                redisClusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
            }
            // 支持自适应集群拓扑刷新和静态刷新源
            ClusterTopologyRefreshOptions clusterTopologyRefreshOptions =
                ClusterTopologyRefreshOptions.builder().enableAllAdaptiveRefreshTriggers().build();
            ClusterClientOptions clusterClientOptions =
                ClusterClientOptions.builder().topologyRefreshOptions(clusterTopologyRefreshOptions).build();

            GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
            genericObjectPoolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
            genericObjectPoolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
            genericObjectPoolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
            genericObjectPoolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
            LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig).clientOptions(clusterClientOptions).build();
            lettuceConnectionFactory =
                new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
            lettuceConnectionFactory.setShareNativeConnection(shareNativeConnection);
        } else if (StringUtils.isNotBlank(redisProperties.getHost()) && redisProperties.getPort() != 0) {
            String host = redisProperties.getHost();
            int port = redisProperties.getPort();
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
            if (StringUtils.isNotEmpty(redisProperties.getPassword())) {
                redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
            }
            lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        } else {
            log.error("please config redis cluster nodes or host and port !!!");
        }
        return lettuceConnectionFactory;
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                if (e instanceof RedisConnectionFailureException) {
                    log.error("Method:handleCacheGetError redis has lose connection:", e);
                    return;
                } else if (e instanceof PoolException) {
                    log.error("Method:handleCacheGetError redis has lose connection:", e);
                    return;
                } else if (e instanceof RedisException) {
                    log.error("Method:handleCacheGetError redis has lose connection:", e);
                    return;
                } else {
                    log.error("Method:handleCacheGetError error", e);
                    return;
                }
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                if (e instanceof RedisConnectionFailureException) {
                    log.error("Method:handleCachePutError redis has lose connection:", e);
                    return;
                } else if (e instanceof PoolException) {
                    log.error("Method:handleCachePutError redis has lose connection:", e);
                    return;
                } else if (e instanceof RedisException) {
                    log.error("Method:handleCachePutError redis has lose connection:", e);
                    return;
                }
                log.error("Method:handleCachePutError error", e);
                return;
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                if (e instanceof RedisConnectionFailureException) {
                    log.error("Method:handleCacheEvictError redis has lose connection:", e);
                    return;
                } else if (e instanceof PoolException) {
                    log.error("Method:handleCacheEvictError redis has lose connection:", e);
                    return;
                } else if (e instanceof RedisException) {
                    log.error("Method:handleCacheEvictError redis has lose connection:", e);
                    return;
                }
                log.error("Method:handleCacheEvictError error", e);
                return;
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                if (e instanceof RedisConnectionFailureException) {
                    log.error("Method:handleCacheClearError redis has lose connection:", e);
                    return;
                } else if (e instanceof PoolException) {
                    log.error("Method:handleCacheClearError redis has lose connection:", e);
                    return;
                } else if (e instanceof RedisException) {
                    log.error("Method:handleCacheClearError redis has lose connection:", e);
                    return;
                }
                log.error("Method:handleCacheClearError error", e);
                return;
            }
        };
        return cacheErrorHandler;
    }
}
package com.test.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.test.bean.CacheTestBean;
import java.util.Optional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * @date 2020/6/27 17:16:00
 * @description TODO
 */
@Slf4j
@Data
public class GuavaCacheManager {

    private CacheBuilder cacheBuilder;

    private Cache jvmCache;

    public void initCache() {
        if (jvmCache == null) {
            jvmCache = cacheBuilder.build(new CacheLoader<String, Optional<CacheTestBean>>() {
                @Override
                public Optional<CacheTestBean> load(String id) {
                    CacheTestBean bean = null;
                    if (StringUtils.isNotBlank(id)) {
                        bean = new CacheTestBean();
                        bean.setId(id);
                        bean.setName("Name" + id);
                    }
                    return Optional.ofNullable(bean);
                }
            });
        }
    }

    public Object getCache(String key) {
        Object object = jvmCache.getIfPresent(key);
        return object;
    }

    public void putCache(String key, Object object) {
        jvmCache.put(key, object);
    }
}

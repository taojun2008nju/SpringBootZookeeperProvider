package com.test.util;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author chenx
 * @date 2020/5/18
 */
public interface KeyGenerator {

    /**
     * 生成缓存key
     *
     * @param method
     * @param key
     * @param params
     * @return String
     * @author chenx
     * @date 2020/5/18 19:26
     */
    String generatorKey(Method method, String key, Object[] params);


    /**
     * 生成缓存key
     *
     * @param method
     * @param key
     * @param params
     * @return String
     * @author chenx
     * @date 2020/5/27 19:26
     */
    List<String> generatorKeys(Method method, String key, Object[] params);

}

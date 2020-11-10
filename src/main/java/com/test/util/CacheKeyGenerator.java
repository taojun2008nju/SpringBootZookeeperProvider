package com.test.util;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

/**
 * @author chenx
 * @date 2020/5/18
 */
@Slf4j
@Component
public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public String generatorKey(Method method, String key, Object[] params) {
        if(StringUtils.isEmpty(key)){
            return getHashCode(method,params);
        }
        String cacheKey = SpElUtil.getValue(key, params, method);
        if(StringUtils.isNotEmpty(cacheKey)){
            return cacheKey;
        }
        return getHashCode(method,params);
    }

    @Override
    public List<String> generatorKeys(Method method, String key, Object[] params) {
        if(StringUtils.isEmpty(key)){
            return Lists.newArrayList(getHashCode(method,params));
        }
        List<String> cacheKeys = SpElUtil.getValues(key, params, method);
        if(CollectionUtils.isNotEmpty(cacheKeys)){
            return cacheKeys;
        }
        return Lists.newArrayList(getHashCode(method,params));

    }

    private String getHashCode(Method method,Object[] params) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(method.toString());

        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        if (ArrayUtils.isNotEmpty(parameterNames) && ArrayUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.length; i++) {
                stringBuffer.append(parameterNames[i]).append("=").append(JsonUtils.toJson(params[i])).append("&");
            }
        }
        return String.valueOf(stringBuffer.toString().hashCode());
    }
}

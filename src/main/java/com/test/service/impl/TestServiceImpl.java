package com.test.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.test.annotation.GuavaCache;
import com.test.manage.TestManage;
import com.test.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020/5/10 20:40:00
 * @description TODO
 */
@Slf4j
@Service(interfaceClass = TestService.class) //dubbo的service，注入接口
@Component //spring注解，扫描包
public class TestServiceImpl implements TestService {

    @Autowired
    private TestManage testManage;

    @Override
//    @GuavaCache(cacheKey = "#text")
    public String test(String text) {
        log.info("Method:TestServiceImpl.test :{}", text);
        String result = testManage.test(text);
        return "Success! Your String is " + result;
    }

    @Override
    @CacheEvict(value = "Redis:", key = "#text")
    public String testRedisFlush(String text) {
        log.info("Method:TestServiceImpl.testRedisFlush :{}", text);
        String result = testManage.test(text);
        return "Success! Your String is " + result;
    }

    @Override
    @Cacheable(value = "Redis:", key = "#text")
    public String testRedisQuery(String text) {
        log.info("Method:TestServiceImpl.testRedisQuery :{}", text);
        String result = testManage.test(text);
        return "Success! Your String is " + result;
    }
}

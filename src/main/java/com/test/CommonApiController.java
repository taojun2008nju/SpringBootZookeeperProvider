package com.test;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.test.bean.CacheTestBean;
import com.test.cache.GuavaCacheManager;
import com.test.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CommonApiController {

    @Autowired
    private TestService testService;

    @Autowired
    private GuavaCacheManager guavaCacheManager;

    @RequestMapping(value = "/testApi")
    @ResponseBody
    public String testApi() {
        CacheTestBean bean1 = new CacheTestBean();
        bean1.setId("1");
        bean1.setName("testa");
        guavaCacheManager.putCache("1", bean1);
        CacheTestBean object = (CacheTestBean)guavaCacheManager.getCache("1");
        Object object2 = guavaCacheManager.getCache("2");

        String result = testService.test("a");

        log.info("Method:testApi:");
        return "Hello World";
    }

    @RequestMapping(value = "/testRedisQuery")
    @ResponseBody
    public String testRedisQuery() {
        String result = testService.testRedisQuery("a");

        log.info("Method:testRedis:");
        return "Hello World Redis";
    }

    @RequestMapping(value = "/testRedisFlush")
    @ResponseBody
    public String testRedisFlush() {
        String result = testService.testRedisFlush("a");

        log.info("Method:testRedis:");
        return "Hello World Redis";
    }

    /**
     * 熔断之后执行的方法
     * @return
     */
    public String testFallback(String id){
        return "熔断--服务正忙，请求稍后再试！";
    }

    @HystrixCommand(fallbackMethod = "testFallback", ignoreExceptions = NullPointerException.class)
    @RequestMapping(value = "/testHystrix",method = RequestMethod.GET)
    public String testHystrix(String id) {
        System.out.println("Method:testHystrix");
        if (id.equalsIgnoreCase("1")) {
            throw new NullPointerException();
        } else {
            throw new ArithmeticException();
        }
    }
}
package com.test.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @date 2020/9/19 18:02:00
 * @description Kafka初始化配置
 */

@Configuration
public class KafkaConfig {

    //创建TopicName为SIMPLE的Topic并设置分区数为3以及副本数为1
    @Bean//通过bean创建(bean的名字为initialTopic)
    public NewTopic simpleTopic() {
        return new NewTopic("SIMPLE", 3, (short) 1);
    }

}

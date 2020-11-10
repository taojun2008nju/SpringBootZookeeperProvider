package com.test.kafka;

import com.test.constant.KafkaConstants;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author Administrator
 * @date 2020/7/26 16:10:00
 * @description Kafka消息生产者
 */
@Slf4j
@Component
@EnableScheduling
public class KafkaMsgProvider {

    @Autowired
    @Qualifier(value = "kafkaTemplate")
    private KafkaTemplate kafkaTemplate;


    @Scheduled(cron = "0/10 * * * * ?")
    public void send() {
        String message = UUID.randomUUID().toString();
        ListenableFuture listenableFuture = kafkaTemplate.send(KafkaConstants.TOPIC_SIMPLE, message);
        listenableFuture.addCallback(
            o -> log.info("消息发送成功,{}", o.toString()),
            throwable -> log.info("消息发送失败,{}" + throwable.getMessage())
        );
    }
}

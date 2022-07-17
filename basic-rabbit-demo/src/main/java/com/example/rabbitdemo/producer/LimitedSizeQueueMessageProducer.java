package com.example.rabbitdemo.producer;

import com.example.rabbitdemo.data.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.example.rabbitdemo.config.LimitedSizeQueueConfig.EXCHANGE_NAME;
import static com.example.rabbitdemo.config.LimitedSizeQueueConfig.QUEUE_NAME;

@Component
@Slf4j
public class LimitedSizeQueueMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public LimitedSizeQueueMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSyncMessage2() {
        MyMessage message = new MyMessage();
        message.setContent("Content3");
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            Boolean confirmation = rabbitTemplate.invoke(ops -> {
                ops.convertAndSend(EXCHANGE_NAME, QUEUE_NAME, "Content" + finalI);
                return ops.waitForConfirms(1_000);
            });
            log.info("Confirmation received {} ", confirmation);
        }

        Boolean confirmation = rabbitTemplate.invoke(ops -> {
            ops.convertAndSend(EXCHANGE_NAME, QUEUE_NAME, "Content3");
            return ops.waitForConfirms(1_000);
        });
        log.info("Confirmation received {} ", confirmation);
    }
}

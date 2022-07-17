package com.example.rabbitdemo;

import com.example.rabbitdemo.producer.LimitedSizeQueueMessageProducer;
import com.example.rabbitdemo.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    private MessageProducer producer;

    @Autowired
    private LimitedSizeQueueMessageProducer limitedProducer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        producer.sendAsyncMessage();
        producer.sendAsyncTransientMessage();
        producer.sendAsyncMessage();
//        producer.sendAsyncMessageAndTraceReturnsAndConfirms();
//        limitedProducer.sendSyncMessage2();
    }
}

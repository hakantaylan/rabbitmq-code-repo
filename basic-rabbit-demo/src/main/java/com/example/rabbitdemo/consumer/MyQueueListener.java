package com.example.rabbitdemo.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyQueueListener {

    @RabbitListener(queues = {"q.test-queue"})
    public void onMessage(String message, Channel channel){ // channel manuel ACK gerekiyorsa kullanılır.
        System.out.println("received:" +  message);
//        throw new RuntimeException();
        throw new AmqpRejectAndDontRequeueException("Dont requeue this message, just ignore it. Remove or DLQ");
    }

//    @RabbitListener(queues = "debug")
//    public void processMessage1(@Payload String body, @Header String token) {
//        System.out.println("body: "+body);
//        System.out.println("token: "+token);
//    }
//
//    @RabbitListener(queues = "debug")
//    public void processMessage1(@Payload String body, @Headers Map<String,Object> headers) {
//        System.out.println("body: "+body);
//        System.out.println("Headers: "+headers);
//    }
}

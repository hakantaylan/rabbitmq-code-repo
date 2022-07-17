package com.example.cluster;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void run(ApplicationArguments args) {
        Message message = MessageBuilder.withBody("Deneme".getBytes(StandardCharsets.UTF_8)).build();
        rabbitTemplate.convertAndSend("x.test-exchange", "routMe", message);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }


    @Scheduled(fixedDelay = 1_000)
    public void produceMessage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateMessage = LocalDateTime.now().format(formatter);
//        Message message = MessageBuilder.withBody(formatDateTime.getBytes(StandardCharsets.UTF_8)).build();
        rabbitTemplate.convertAndSend("x.test-exchange", "routMe", dateMessage);
    }
}

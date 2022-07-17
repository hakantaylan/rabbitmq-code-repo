package com.example.rabbitdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LimitedSizeQueueConfig {

    public static final String QUEUE_NAME = "q.max-length-queue";
    public static final String EXCHANGE_NAME = "x.max-length-exchange";
    public static final String DLQ_NAME = "demo-dead-letter-exchange-queue";
    public static final String DLK_NAME = "deadLetter-key";

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_NAME)
                .lazy() // doğrudan diske yazılacak. Daha yavaş ama DLQ'nun da diğer kuyruklar kadar hızlı olmak gibi bir derdi yok
                .build();
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange("deadLetterExchange")
                .internal() // Bu exchange dış dünyadan erişilmeyeceği için internal
                .build();
//        return new DirectExchange("deadLetterExchange");
    }

    @Bean
    Binding DLQbinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLK_NAME);
    }

    @Bean
    public Queue createMaxLengthQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .maxLength(2)
//                .overflow(QueueBuilder.Overflow.rejectPublish)
                .overflow(QueueBuilder.Overflow.dropHead)
                .ttl(5_000)   // Kuyruktaki mesaj 5 sn. içinde işlenmezse silinir.
                .expires(15_000)    // Producer veya Consumer 15. saniye etkileşime geçmezse kuyruk silinir
                .deadLetterExchange("deadLetterExchange")
                .deadLetterRoutingKey(DLK_NAME)
                .build();
    }

    //    @Bean
    public Queue createMaxLengthQueueSecondWay() {
        return QueueBuilder.durable(QUEUE_NAME)
                .maxLength(2)
                .overflow(QueueBuilder.Overflow.dropHead)
                .withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", DLK_NAME)
                .build();
    }

    @Bean
    public DirectExchange limitedExchange() {
//        return new DirectExchange(EXCHANGE_NAME, false, false);
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).build();
    }

    @Bean
    Binding limitedBinding(Queue createMaxLengthQueue, DirectExchange limitedExchange) {
        return BindingBuilder.bind(createMaxLengthQueue).to(limitedExchange).withQueueName();
    }

}

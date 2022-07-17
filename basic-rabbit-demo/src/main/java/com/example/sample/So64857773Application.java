package com.example.sample;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class So64857773Application {

    public static void main(String[] args) {
        SpringApplication.run(So64857773Application.class, args);
    }

    @Bean
    Queue queue() {
        return new Queue("so64857773");
    }

    @Bean
    public DirectExchange exchange() {
//        return new DirectExchange(EXCHANGE_NAME, false, false);
        return ExchangeBuilder.directExchange("e").build();
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).withQueueName();
    }

    @Bean
    ApplicationRunner runner(CachingConnectionFactory cf, RabbitTemplate template) {
        return args -> {
            StopWatch watch = new StopWatch();
            cf.createConnection().close();
            watch.start("correlated");
            for (int i = 0; i < 1_000; i++) {
                CorrelationData cd = new CorrelationData();
                template.convertAndSend("e", "so64857773", "test message", cd);
                cd.getFuture().get(1, TimeUnit.SECONDS);
            }
            watch.stop();

            cf.resetConnection();
            cf.setPublisherConfirmType(ConfirmType.SIMPLE);
            RabbitTemplate simpleTemplate = new RabbitTemplate(cf);
            cf.createConnection().close();
            watch.start("simple");
            for (int i = 0; i < 1_000; i++) {
                simpleTemplate.invoke(ops -> {
                    ops.convertAndSend("e", "so64857773", "test message");
                    return ops.waitForConfirms(1_000);
                });
            }
            watch.stop();

            System.out.println(watch.prettyPrint());
        };
    }

}

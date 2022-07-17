package com.example.cluster;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_NAME = "q.test-queue";
    public static final String EXCHANGE_NAME = "x.test-exchange";

    @Bean
    public Queue queue() {
//        return new Queue(QUEUE_NAME, false);
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public DirectExchange exchange() {
//        return new DirectExchange(EXCHANGE_NAME, false, false);
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).build();
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("routMe");
    }

//    @Bean
//    public ConnectionFactory connectionFactory(){
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("127.0.0.1", 5672);
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        connectionFactory.setVirtualHost("/");
//
//        connectionFactory.setPublisherReturns(true);
//        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
//        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
//        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.NONE);
//
//        return connectionFactory;
//    }

//    @Bean
//    public Jackson2JsonMessageConverter converter(){
//        return new Jackson2JsonMessageConverter();
//    }

//    @Bean
//    public MessageConverter messageConverter() {
//        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
//        return new Jackson2JsonMessageConverter(objectMapper);
//    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory listenerContainerFactory() {
//        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
//        containerFactory.setConnectionFactory(connectionFactory());
//        containerFactory.setMessageConverter(messageConverter());
//        containerFactory.setMaxConcurrentConsumers(10);
//        containerFactory.setConcurrentConsumers(5);
//        containerFactory.setAutoStartup(true);
//        containerFactory.setPrefetchCount(10);
//        containerFactory.setDefaultRequeueRejected(false);
//        /*
//        containerFactory.setAdviceChain(RetryInterceptorBuilder.stateless()
//                .maxAttempts(3)
//                .recoverer(new RejectAndDontRequeueRecoverer())
//                .build());
//
//         */
//
//        //If there is a problem, don't re-queue. send them to DLX.
//        //containerFactory.setDefaultRequeueRejected(false);
//
//        return containerFactory;
//    }

//    @Bean
//    public RetryOperationsInterceptor retryInterceptor(){
//        return RetryInterceptorBuilder.stateless().maxAttempts(3)
//                .backOffOptions(2000, 2.0, 100000)
//                .recoverer(new RejectAndDontRequeueRecoverer())
//                .build();
//    }
}

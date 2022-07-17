package com.example.rabbitdemo.producer;

import com.example.rabbitdemo.data.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.example.rabbitdemo.config.RabbitConfig.EXCHANGE_NAME;
import static com.example.rabbitdemo.config.RabbitConfig.QUEUE_NAME;

@Component
@Slf4j
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAsyncTransientMessage(){
        MyMessage message = new MyMessage();
        message.setContent("Content1");
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, QUEUE_NAME, "Transient Content", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
                return message;
            }
        });
    }

    public void sendAsyncTransientMessage2(){
        String strMessage = "Transient Message Content";
        Message msg = new Message(strMessage.getBytes(StandardCharsets.UTF_8));
        msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, QUEUE_NAME, msg);
    }

    public void sendAsyncMessage(){
        MyMessage message = new MyMessage();
        message.setContent("Content1");
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, QUEUE_NAME, "Content1");
    }

    public void sendAsyncMessageAndTraceReturnsAndConfirms(){
        MyMessage message = new MyMessage();
        message.setContent("Content1");
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String reason) {
                log.info("correlationData: {}", correlationData);
                log.info("ack: {}", ack);
                log.info("reason: {}", reason);
            }
        });
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.info("Returned message {} ", returnedMessage);
                System.out.println("================");
                System.out.println("message = " + returnedMessage.getMessage());
                System.out.println("replyCode = " + returnedMessage.getReplyCode());
                System.out.println("replyText = " + returnedMessage.getReplyText());
                System.out.println("exchange = " + returnedMessage.getExchange());
                System.out.println("routingKey = " + returnedMessage.getRoutingKey());
                System.out.println("================");
            }
        });
        rabbitTemplate.convertAndSend(EXCHANGE_NAME + "a", QUEUE_NAME, message, new CorrelationData(message.getId()));
    }

    /**
     * Producer, Exchange ve Queue arasında oluşabilecek hataları sync olarak bekleyen metod.
     * Eğer confirm false ise producer-exchange arasında bir hata vardır. confirm.getReason() ile detayı görülebilir.
     * Eğer confirm true ise correlationData.getReturned() == null diyerek mesaj geri dönmüş mü diye bakmak lazım. null ise mesaj hata olmadan kuyruğa bırakılmış demektir.
     * null'dan faklı ise mesaj exchange tarafından kuyruğa iletilememiş ve bize geri gönderilmiş demektir. correlationData.getReturned() nesnesi ile detayına bakılabilir.
     * @throws Exception
     */
    public void sendSyncMessage() throws Exception {
        MyMessage message = new MyMessage();
        message.setContent("Content2");
        CorrelationData correlationData = new CorrelationData(message.getId());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, QUEUE_NAME, "Content2", correlationData);
        CorrelationData.Confirm confirm = correlationData.getFuture().get(1, TimeUnit.SECONDS);// This makes it sync
        log.info("Confirmation received {} ", confirm);
        log.info("Returned message {} ", correlationData.getReturned());
    }

    public void sendSyncMessage2() {
        MyMessage message = new MyMessage();
        message.setContent("Content3");
        CorrelationData correlationData = new CorrelationData(message.getId());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, QUEUE_NAME, "Content3");  // confirms must be set as simple
        boolean confirmation = rabbitTemplate.waitForConfirms(1_000);
        log.info("Confirmation received {} ", confirmation);
    }

    public void sendSyncMessage3() {
        MyMessage message = new MyMessage();
        message.setContent("Content3");
        CorrelationData correlationData = new CorrelationData(message.getId());
        Boolean confirmation = rabbitTemplate.invoke(ops -> {
            ops.convertAndSend(EXCHANGE_NAME, QUEUE_NAME, "Content3");
            return ops.waitForConfirms(1_000);
        });
        log.info("Confirmation received {} ", confirmation);
    }
}

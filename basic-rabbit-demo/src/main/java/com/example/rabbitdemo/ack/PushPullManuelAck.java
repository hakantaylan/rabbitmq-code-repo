package com.example.rabbitdemo.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;

public class PushPullManuelAck {
    /*
        basicAck: This is to manually confirm that the message has been successfully consumed.
        This method has two parameters: the first parameter represents the id of the message;
        the second parameter, multiple, if it is false, means that only the current message is successfully consumed.
        If it is true, it means All messages that have not been acknowledged by the current consumer before the current message are consumed successfully.

        basicNack: This is to tell RabbitMQ that the current message has not been successfully consumed.
        This method has three parameters: the first parameter represents the id of the message;
        the second parameter multiple If it is false, it means that only the consumption of the current message is rejected.
        If it is true, then Indicates that all messages that have not been confirmed by the current consumer before the current message are rejected;
        the third parameter requeue is the same as the previous one, whether the rejected messages are re-queued.
    */
//    spring.rabbitmq.listener.simple.acknowledge-mode=manual
    // Push model manual Ack example
    @RabbitListener(queues = {"queue"})
    public void handle3(Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String s = new String(message.getBody());
            System.out.println("s = " + s);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    //    Pull model manual Ack
    public void receive2() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(); // Don't do this. Autowire it!!!
        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(true);
        long deliveryTag = 0L;
        try {
            GetResponse getResponse = channel.basicGet("queue", false);
            deliveryTag = getResponse.getEnvelope().getDeliveryTag();
            System.out.println("o = " + new String((getResponse.getBody()), "UTF-8"));
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

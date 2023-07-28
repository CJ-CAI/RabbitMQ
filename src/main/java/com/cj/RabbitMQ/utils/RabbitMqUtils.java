package com.cj.RabbitMQ.utils;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
            ConnectionFactory factory=new ConnectionFactory();
            factory.setHost("192.168.137.250");
            factory.setUsername("admin");
            factory.setPassword("123");
            Connection connection=factory.newConnection();
            Channel channel=connection.createChannel();
            return channel;
    }
    public static DeliverCallback getDeliverCallback() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println(message);
        };
    return deliverCallback;
    }
    public static CancelCallback getCancelCallback () {
        CancelCallback  cancelCallback  = (consumerTag) -> {
            System.out.println("消息中断");
        };
        return cancelCallback;
    }
}

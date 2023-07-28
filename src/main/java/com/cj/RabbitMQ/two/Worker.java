package com.cj.RabbitMQ.two;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker {
    private static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("worker_2 waiting for message.......");
        Channel channel= RabbitMqUtils.getChannel();
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消息中断");
        };
        channel.basicConsume(QUEUE_NAME,true,RabbitMqUtils.getDeliverCallback(),RabbitMqUtils.getCancelCallback());
    }
}

package com.cj.RabbitMQ.six;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

public class Consumer02 {
    private static final String QUEUE_DEAD_NAME="dead-queue";
    private static final String EXCHANGE_DEAD_NAME="dead";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        channel.queueBind(QUEUE_DEAD_NAME,EXCHANGE_DEAD_NAME,"lisi");
        channel.basicConsume(QUEUE_DEAD_NAME,false,RabbitMqUtils.getDeliverCallback(),RabbitMqUtils.getCancelCallback());
    }
}

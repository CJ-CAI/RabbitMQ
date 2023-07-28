package com.cj.RabbitMQ.five;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs01 {
    private static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        String channel_name=channel.queueDeclare().getQueue();
        channel.queueBind(channel_name,EXCHANGE_NAME,"");
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        channel.basicConsume("",false,RabbitMqUtils.getDeliverCallback(),RabbitMqUtils.getCancelCallback());
    }
}

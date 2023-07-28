package com.cj.RabbitMQ.five;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveDirect01 {
    private static final String EXCHANGE_NAME="exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queue_name="direct";
        channel.queueDeclare(queue_name,false,false,false,null);
        channel.queueBind(queue_name,EXCHANGE_NAME,"error");
        channel.basicConsume(queue_name,true,RabbitMqUtils.getDeliverCallback(),RabbitMqUtils.getCancelCallback());
    }
}

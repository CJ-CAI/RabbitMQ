package com.cj.RabbitMQ.six;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String EXCHANGE_NAME="normal";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
//        AMQP.BasicProperties basicProperties=new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i=0;i<10;i++){
            String message="info"+i;
            channel.basicPublish(EXCHANGE_NAME,"zhangsan",null,message.getBytes());
        }
    }
}

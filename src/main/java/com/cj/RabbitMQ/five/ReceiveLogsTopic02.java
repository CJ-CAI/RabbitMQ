package com.cj.RabbitMQ.five;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsTopic02 {
    private static final String exchange_topic_name="exchange_topic";
    private static final String queue_topic_name="Q2";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        channel.exchangeDeclare(exchange_topic_name, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(queue_topic_name,false,false,false,null);
        channel.queueBind(queue_topic_name,exchange_topic_name,"*.*.rabbit");
        channel.queueBind(queue_topic_name,exchange_topic_name,"lazy.#");
        channel.basicConsume(queue_topic_name,true,RabbitMqUtils.getDeliverCallback(),RabbitMqUtils.getCancelCallback());
    }
}

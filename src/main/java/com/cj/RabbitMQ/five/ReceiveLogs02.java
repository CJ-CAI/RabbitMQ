package com.cj.RabbitMQ.five;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs02 {
    private static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        String channel_name=channel.queueDeclare().getQueue();
        channel.queueBind(channel_name,EXCHANGE_NAME,"");
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        DeliverCallback deliverCallback=( consumerTag, message)->{
            String envelop=new String(message.getBody(),"UTF-8");
            File file=new File("C:\\work\\rabbitmq_info.txt");
            FileUtils.writeStringToFile(file, envelop,"UTF-8");
        };
        channel.basicConsume("",false,deliverCallback,RabbitMqUtils.getCancelCallback());
    }
}

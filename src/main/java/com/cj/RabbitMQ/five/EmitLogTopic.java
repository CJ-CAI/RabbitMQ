package com.cj.RabbitMQ.five;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {
    private static final String exchange_topic_name="exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        channel.exchangeDeclare(exchange_topic_name, BuiltinExchangeType.TOPIC);
        Map<String,String> map=new HashMap<>();
        map.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        map.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        map.put("quick.orange.fox","被队列 Q1 接收到");
        map.put("lazy.brown.fox","被队列 Q2 接收到");
        map.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        map.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        map.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        map.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key=entry.getKey();
            String message=entry.getValue();
            channel.basicPublish(exchange_topic_name,key,null,message.getBytes());
        }
    }
}

package com.cj.RabbitMQ.six;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer01 {
    private static final String EXCHANGE_NAME="normal";
    private static final String EXCHANGE_DEAD_NAME="dead";
    private static final String QUEUE_NAME="normal-queue";
    private static final String QUEUE_DEAD_NAME="dead-queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(EXCHANGE_DEAD_NAME,BuiltinExchangeType.DIRECT);
        Map<String, Object> arguments=new HashMap<>();
        //正常队列设置死信交换机 参数 key 是固定值
        arguments.put("x-dead-letter-exchange", EXCHANGE_DEAD_NAME);
        //正常队列设置死信 routing-key 参数 key 是固定值
        arguments.put("x-dead-letter-routing-key", "lisi");
        arguments.put("x-max-length",6);
        channel.queueDeclare(QUEUE_NAME,false,false,false,arguments);
        channel.queueDeclare(QUEUE_DEAD_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"zhangsan");
        DeliverCallback deliverCallback=(consumerTag, message)->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String meg=new String(message.getBody());
            if (meg.equals("info6")){
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
                System.out.println("reject:"+meg);
            }
            else{
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                System.out.println(meg);
            }

        };
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,RabbitMqUtils.getCancelCallback());
    }
}

package com.cj.RabbitMQ.one;

import com.rabbitmq.client.*;
public class Consumer {
    private final  static String QUEUE_NAME="hello";
    public static void main(String[] args) {
        try{
            ConnectionFactory factory=new ConnectionFactory();
            factory.setHost("192.168.137.250");
            factory.setUsername("admin");
            factory.setPassword("123");
            Connection connection= factory.newConnection();
            Channel channel=connection.createChannel();
            System.out.println("waiting for message");
            DeliverCallback deliverCallback=(consumerTag,delivery)->{
                String message=new String(delivery.getBody());
                System.out.println(message);
            };
            CancelCallback cancelCallback=(consumerTag)->{
                System.out.println("消息中断");
            };
            /**
             * 消费者消费消息
             * 1.消费哪个队列
             * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
             * 3.消费者未成功消费的回调
             */
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

package com.cj.RabbitMQ.three;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class worker_2 {
    private static final String QUEUE_NAME="lose";

    public static void main(String[] args) {
        try {
            System.out.println("work2 waiting for.......");
            Channel channel= RabbitMqUtils.getChannel();
            channel.basicQos(2);
            DeliverCallback deliverCallback= (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };
            channel.basicConsume(QUEUE_NAME,false,deliverCallback,RabbitMqUtils.getCancelCallback());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

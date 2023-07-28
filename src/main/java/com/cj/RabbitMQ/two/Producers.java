package com.cj.RabbitMQ.two;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Producers {
    private static final String QUEUE_NAME="hello";
    public static void main(String[] args) {
        try {
            Channel channel= RabbitMqUtils.getChannel();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            Scanner scanner=new Scanner(System.in);
            while (scanner.hasNext()){
                String message= scanner.next();
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}

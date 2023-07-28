package com.cj.RabbitMQ.three;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

public class producers {
    private static final String QUEUE_NAME="lose";
    public static void main(String[] args) {
        try{
            Channel channel= RabbitMqUtils.getChannel();
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            Scanner scanner=new Scanner(System.in);
            while (scanner.hasNext()){
                String message=scanner.next();
                channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

package com.cj.RabbitMQ.five;

import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class EmitLog {
    private static final String EXCHANGE_NAME="exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            Scanner scanner=new Scanner(System.in);
            while (scanner.hasNext()){
                String message= scanner.next();
                channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

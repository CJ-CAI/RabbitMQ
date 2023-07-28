package com.cj.RabbitMQ.four;


import com.cj.RabbitMQ.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

public class producer_ack {
    private final static String QUEUE_NAME="hello";
    private final static int PUBLISH_COUNT=1000;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        producer_ack producerAck=new producer_ack();
        Channel channel= RabbitMqUtils.getChannel();
//        producerAck.publish_synchronized_ack(channel);//720ms
//        producerAck.publish_multiple_ack(channel);//89ms
//        producerAck.publish_asynchronous_ack(channel);//70ms
    }
    public void publish_synchronized_ack(Channel channel) throws IOException, InterruptedException {
        long begin = System.currentTimeMillis();
        channel.confirmSelect();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        for (int i=0;i<PUBLISH_COUNT;i++){
            String message=i+"";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            boolean flag=channel.waitForConfirms();
            if (flag==true)
                System.out.println("publish success");
        }
        long end = System.currentTimeMillis();
        System.out.println("publish consume time:"+(end-begin));
    }

    public void publish_multiple_ack(Channel channel) throws IOException, InterruptedException {
        long begin = System.currentTimeMillis();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.confirmSelect();
        for (int i=0;i<PUBLISH_COUNT;i++){
            String message=i+"";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            if (i%100==0){
                boolean flag=channel.waitForConfirms();
                if (flag==true)
                    System.out.println("publish success");}
        }
        long end = System.currentTimeMillis();
        System.out.println("publish consume time:"+(end-begin));
    }
    public void publish_asynchronous_ack(Channel channel) throws IOException {
        long begin = System.currentTimeMillis();
        ConcurrentSkipListMap<Long,String> outstandingConfirms=new ConcurrentSkipListMap<>();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.confirmSelect();


        ConfirmCallback ackCallback=(sequenceNumber, multiple)->{
         if (multiple){
             ConcurrentNavigableMap<Long, String> confirmed =outstandingConfirms.headMap(sequenceNumber,true);
             confirmed.clear();
         }
             else
                 outstandingConfirms.remove(sequenceNumber);
        };
        ConfirmCallback nackCallback=(sequenceNumber, multiple)->{
            String message=outstandingConfirms.get(sequenceNumber);
            System.out.println("发布的消息"+message+"未被确认，序列号"+sequenceNumber);
        };


        channel.addConfirmListener(ackCallback,nackCallback);
        for (int i=0;i<PUBLISH_COUNT;i++){
            String message=i+"";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }
        long end = System.currentTimeMillis();
        System.out.println("publish consume time:"+(end-begin));
    }

}

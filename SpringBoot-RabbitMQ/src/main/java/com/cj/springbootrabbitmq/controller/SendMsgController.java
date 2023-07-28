package com.cj.springbootrabbitmq.controller;

import com.cj.springbootrabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("ttl")
public class SendMsgController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("send1/{message}")
    public void sendMsg1(@PathVariable String message){
        log.info("当前时间：{},发送一条信息给两个 TTL 队列:{}", new Date(), message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.EXCHANGE_X, "XA", "消息来自 ttl 为 10S 的队列: "+message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.EXCHANGE_X, "XB", "消息来自 ttl 为 40S 的队列: "+message);
    }
    @GetMapping("send2/{message}/{ttl}")
    public void sendMsg2(@PathVariable String message,@PathVariable String ttl){
        rabbitTemplate.convertAndSend(TtlQueueConfig.EXCHANGE_X, "XC", message,correlationData->{
            correlationData.getMessageProperties().setExpiration(ttl);
            return correlationData;
        });
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}", new Date(),ttl, message);
    }
    @GetMapping("send3/{message}/{ttl}")
    public void sendMsg3(@PathVariable String message,@PathVariable Integer ttl){
        rabbitTemplate.convertAndSend(TtlQueueConfig.DELAYED_EXCHANGE_NAME, TtlQueueConfig.DELAYED_ROUTING_KEY, message,correlationData->{
            correlationData.getMessageProperties().setDelay(ttl);
            return correlationData;
        });
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 {}:{}", new Date(),ttl,TtlQueueConfig.DELAYED_QUEUE_NAME, message);
    }
    @GetMapping("send4/{message}")
    public void sendMsg4(@PathVariable String message){

        rabbitTemplate.convertAndSend(TtlQueueConfig.CONFIRM_EXCHANGE_NAME, TtlQueueConfig.CONFIRM_ROUTING_NAME, message+"1");
        rabbitTemplate.convertAndSend(TtlQueueConfig.CONFIRM_EXCHANGE_NAME, TtlQueueConfig.CONFIRM_ROUTING_NAME, message+"2");
        rabbitTemplate.convertAndSend(TtlQueueConfig.CONFIRM_EXCHANGE_NAME, TtlQueueConfig.CONFIRM_ROUTING_NAME, message+"3",correlationData->{
            correlationData.getMessageProperties().setPriority(5);
            return correlationData;
        });
        rabbitTemplate.convertAndSend(TtlQueueConfig.CONFIRM_EXCHANGE_NAME, TtlQueueConfig.CONFIRM_ROUTING_NAME, message+"4");
        rabbitTemplate.convertAndSend(TtlQueueConfig.CONFIRM_EXCHANGE_NAME, TtlQueueConfig.CONFIRM_ROUTING_NAME, message+"5");
//        CorrelationData correlationData2=new CorrelationData("2");
//        rabbitTemplate.convertAndSend(TtlQueueConfig.CONFIRM_EXCHANGE_NAME+"fail", TtlQueueConfig.CONFIRM_ROUTING_NAME, message,correlationData2);
//        CorrelationData correlationData3=new CorrelationData("3");
//        rabbitTemplate.convertAndSend(TtlQueueConfig.CONFIRM_EXCHANGE_NAME, TtlQueueConfig.CONFIRM_ROUTING_NAME+"fail", message,correlationData3);
//        log.info("当前时间：{},发送一条信息给队列 {}:{}", new Date(),TtlQueueConfig.CONFIRM_QUEUE_NAME, message);
    }
}

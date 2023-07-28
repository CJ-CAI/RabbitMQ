package com.cj.springbootrabbitmq.consumer;

import com.cj.springbootrabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.channels.Channel;
import java.util.Date;

@Slf4j
@Component
public class DeadLetterQueueConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(String msg){
    log.info("当前时间：{},{}收到死信队列信息{}", new Date().toString(), TtlQueueConfig.QUEUE_DEAD_D,msg);
    }

    @RabbitListener(queues = TtlQueueConfig.DELAYED_QUEUE_NAME)
    public void receive_DELAYED_QUEUE(String msg){
        log.info("当前时间：{},{}收到死信队列信息{}", new Date().toString(),TtlQueueConfig.DELAYED_QUEUE_NAME, msg);
    }
}

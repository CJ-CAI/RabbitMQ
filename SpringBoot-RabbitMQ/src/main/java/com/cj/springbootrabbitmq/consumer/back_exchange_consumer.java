package com.cj.springbootrabbitmq.consumer;

import com.cj.springbootrabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Date;

@Slf4j
@Component
public class back_exchange_consumer {
    @RabbitListener(queues = TtlQueueConfig.WARNING_QUEUE_NAME)
    public void warning_put(String msg){
        log.info("当前时间：{},{}收到信息：{}", new Date().toString(),TtlQueueConfig.WARNING_QUEUE_NAME, msg);
    }
//    @RabbitListener(queues = TtlQueueConfig.CONFIRM_QUEUE_NAME)
//    public void receiveD(String msg){
//        log.info("当前时间：{},{}收到信息{}", new Date().toString(), TtlQueueConfig.CONFIRM_QUEUE_NAME,msg);
//    }
}

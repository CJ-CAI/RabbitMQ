package com.cj.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TtlQueueConfig {
    public static final String EXCHANGE_X="X";
    public static final String QUEUE_A="QA";
    public static final String QUEUE_B="QB";
    public static final String QUEUE_C="QC";
    public static final String EXCHANGE_DEAD_Y="Y";
    public static final String QUEUE_DEAD_D="QD";

    @Bean("Exchange_X")
    public DirectExchange Exchange_X(){
        return new DirectExchange(EXCHANGE_X);
    }
    @Bean("Exchange_Y")
    public DirectExchange Exchange_Y(){
        return new DirectExchange(EXCHANGE_DEAD_Y);
    }

    @Bean("Queue_QA")
    public Queue Queue_QA(){
        Map<String, Object> arguments=new HashMap<>();
        //声明当前队列绑定的死信交换机
         arguments.put("x-dead-letter-exchange", EXCHANGE_DEAD_Y);
        //声明当前队列的死信路由 key
         arguments.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL
         arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }
    @Bean
    public Binding  QueueBindingA(@Qualifier("Queue_QA") Queue Queue_QA,@Qualifier("Exchange_X") Exchange Exchange_X){
        return BindingBuilder.bind(Queue_QA).to(Exchange_X).with("XA").noargs();
    }

    @Bean("Queue_QB")
    public Queue Queue_QB(){
        Map<String, Object> arguments=new HashMap<>();
        //声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", EXCHANGE_DEAD_Y);
        //声明当前队列的死信路由 key
        arguments.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL
        arguments.put("x-message-ttl", 20000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }
    @Bean
    public Binding  QueueBindingB(@Qualifier("Queue_QB") Queue Queue_QB,@Qualifier("Exchange_X") Exchange Exchange_X){
        return BindingBuilder.bind(Queue_QB).to(Exchange_X).with("XB").noargs();
    }

    @Bean("Queue_QC")
    public Queue Queue_QC(){
        Map<String, Object> arguments=new HashMap<>();
        //声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", EXCHANGE_DEAD_Y);
        //声明当前队列的死信路由 key
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }
    @Bean
    public Binding  QueueBindingC(@Qualifier("Queue_QC") Queue Queue_QC,@Qualifier("Exchange_X") Exchange Exchange_X){
        return BindingBuilder.bind(Queue_QC).to(Exchange_X).with("XC").noargs();
    }

    @Bean("Queue_QD")
    public Queue Queue_QD(){
        return new Queue(QUEUE_DEAD_D);
    }
    @Bean
    public Binding  QueueBindingD(@Qualifier("Queue_QD") Queue Queue_QD,@Qualifier("Exchange_Y") Exchange Exchange_Y){
        return BindingBuilder.bind(Queue_QD).to(Exchange_Y).with("YD").noargs();
    }

//    ========================================延迟交换机===========================================================
        public static final String DELAYED_QUEUE_NAME = "delayed.queue";
        public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
        public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @Bean("Queue_Delayed")
    public Queue Queue_Delayed(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    @Bean("customExchange")
    public CustomExchange customExchange(){
        Map<String, Object> arguments=new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",false,false,arguments);
    }
    @Bean
    public Binding  QueueBindingDelayed(@Qualifier("Queue_Delayed") Queue Queue_Delayed,@Qualifier("customExchange") CustomExchange customExchange){
        return BindingBuilder.bind(Queue_Delayed).to(customExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
//=========================================================发布确认高级=================================================================
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    public static final String CONFIRM_ROUTING_NAME = "confirm.key";

    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    @Bean
    public DirectExchange directExchange(){
        ExchangeBuilder exchangeBuilder=ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).durable(true);
        return (DirectExchange)exchangeBuilder.build();
    }
    @Bean
    public Queue confirm_queue(){
        //设置队列的最大优先级 最大可以设置到 255 官网推荐 1-10 如果设置太高比较吃内存和 CPU
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).withArgument("x-max-priority", 10).build();
    }
    @Bean
    public Binding confirm_bind(@Qualifier("confirm_queue") Queue confirm_queue,@Qualifier("directExchange") DirectExchange directExchange){
        return BindingBuilder.bind(confirm_queue).to(directExchange).with(CONFIRM_ROUTING_NAME);
    }
//=========================================================发布确认高级-备份交换机=================================================================

    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }
    @Bean
    public Queue back_queue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }
    @Bean
    public Queue warning_queue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }
    @Bean
    public Binding back_bind(@Qualifier("fanoutExchange") FanoutExchange fanoutExchange,@Qualifier("back_queue") Queue back_queue){
        return BindingBuilder.bind(back_queue).to(fanoutExchange);
    }
    @Bean
    public Binding warning_bind(@Qualifier("fanoutExchange") FanoutExchange fanoutExchange,@Qualifier("warning_queue") Queue warning_queue){
        return BindingBuilder.bind(warning_queue).to(fanoutExchange);
    }
}

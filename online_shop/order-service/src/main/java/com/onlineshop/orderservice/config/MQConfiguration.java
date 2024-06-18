package com.onlineshop.orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfiguration {
    static final String queueName = "cancel_order_queue";
    static final String exchangeName = "exchange";
    static final String routingKey = "cancel_order_key";

    @Bean
    public Queue myQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    Exchange exchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}
package com.onlineshop.inventoryservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineshop.inventoryservice.dto.OrderRequest;
import com.onlineshop.inventoryservice.service.InventoryService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MQListener {
    static final String queueName = "cancel_order_queue";
    private InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    @Bean
    Queue myQueue() {
        return new Queue(queueName, true);
    }

    @RabbitListener(queues = queueName)
    public void handleMessage(String message) {
        Logger log = LoggerFactory.getLogger(MQListener.class);
        try {
            OrderRequest orderRequest = objectMapper.readValue(message, OrderRequest.class);
            inventoryService.handleOrderCancelation(orderRequest);
        } catch (Exception e) {
            log.info("Error: {}", e);
        }
    }
}

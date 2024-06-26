package com.onlineshop.inventoryservice.listener;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineshop.inventoryservice.dto.OrderRequest;
import com.onlineshop.inventoryservice.service.InventoryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
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
        try {
            OrderRequest orderRequest = objectMapper.readValue(message, OrderRequest.class);
            inventoryService.handleOrderCancelation(orderRequest);
        } catch (Exception e) {
            log.error("Error: {}", e);
        }
    }
}

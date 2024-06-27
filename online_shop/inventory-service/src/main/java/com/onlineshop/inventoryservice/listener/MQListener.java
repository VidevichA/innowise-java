package com.onlineshop.inventoryservice.listener;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineshop.inventoryservice.dto.OrderDto;
import com.onlineshop.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MQListener {
    static final String queueName = "cancel_order_queue";
    private final InventoryService inventoryService;

    @Bean
    Queue myQueue() {
        return new Queue(queueName, true);
    }

    @RabbitListener(queues = queueName)
    public void handleMessage(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            OrderDto orderRequest = objectMapper.readValue(message, OrderDto.class);
            inventoryService.handleOrderCancelation(orderRequest);
        } catch (Exception e) {
            log.error("Error: + " + e.getMessage());
        }
    }
}

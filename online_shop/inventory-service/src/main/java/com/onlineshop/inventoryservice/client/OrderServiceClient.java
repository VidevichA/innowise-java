package com.onlineshop.inventoryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.onlineshop.inventoryservice.dto.OrderDto;

@FeignClient("ORDER")
public interface OrderServiceClient {

    @GetMapping("/api/v1/order/{orderId}")
    OrderDto getOrderById(@PathVariable("orderId") Long orderId);
}

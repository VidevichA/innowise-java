package com.onlineshop.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "INVENTORY")
public interface InventoryServiceClient {

    @PostMapping("api/v1/inventories/order-processing")
    ResponseEntity<?> substractProductsQuantity(Long orderId);
}

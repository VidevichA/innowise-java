package com.onlineshop.inventoryservice.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.onlineshop.inventoryservice.client.OrderServiceClient;
import com.onlineshop.inventoryservice.dto.CreateInventoryRequest;
import com.onlineshop.inventoryservice.dto.InventoryResponse;
import com.onlineshop.inventoryservice.dto.OrderDto;
import com.onlineshop.inventoryservice.dto.OrderItemDto;
import com.onlineshop.inventoryservice.model.Inventory;
import com.onlineshop.inventoryservice.repository.InventoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final OrderServiceClient orderServiceClient;

    public InventoryResponse addNewProductToInventory(CreateInventoryRequest createInventoryRequest) {
        if (inventoryRepository.findByProductId(createInventoryRequest.getProductId()) != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    "Inventory for productId: " + createInventoryRequest.getProductId() + " already exists");
        }
        Inventory inventory = Inventory.builder().productId(createInventoryRequest.getProductId())
                .quantity(createInventoryRequest.getQuantity()).build();
        inventoryRepository.save(inventory);
        return InventoryResponse.builder().id(inventory.getId()).productId(inventory.getProductId())
                .quantity(inventory.getQuantity()).build();
    }

    @Transactional
    public void substractProductsQuantity(Long orderId) {
        OrderDto orderDto = orderServiceClient.getOrderById(orderId);
        List<OrderItemDto> orderItems = orderDto.getOrderItems();
        for (OrderItemDto orderItem : orderItems) {
            Inventory inventory = inventoryRepository.findByProductId(orderItem.getProductId()).orElseThrow(
                    () -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                            "Inventory for productId: " + orderItem.getProductId() + " not found"));
            inventory.setQuantity(inventory.getQuantity() - orderItem.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    public Set<InventoryResponse> getAllInventories() {
        return StreamSupport.stream(inventoryRepository.findAll().spliterator(), false)
                .map(inventory -> InventoryResponse.builder()
                        .id(inventory.getId())
                        .productId(inventory.getProductId())
                        .quantity(inventory.getQuantity())
                        .build())
                .collect(Collectors.toSet());

    }

    public void deleteInventory(Long inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    public Set<InventoryResponse> getInventoriesByProductIds(List<Long> productIds) {
        return inventoryRepository.findByProductIdIn(productIds).stream()
                .map(optionalInventory -> optionalInventory.orElse(null))
                .filter(inventory -> inventory != null)
                .map(inventory -> InventoryResponse.builder()
                        .id(inventory.getId())
                        .productId(inventory.getProductId())
                        .quantity(inventory.getQuantity())
                        .build())
                .collect(Collectors.toSet());
    }

    public void updateInventoryQuantity(Long inventoryId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
        inventory.setQuantity(quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void handleOrderCancelation(OrderDto orderRequest) {
        for (OrderItemDto orderItemRequest : orderRequest.getOrderItems()) {
            Inventory inventory = inventoryRepository.findByProductId(orderItemRequest.getProductId())
                    .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                            "No inventory found for productId: " + orderItemRequest.getProductId()));
            inventory.setQuantity(inventory.getQuantity() + orderItemRequest.getQuantity());
            inventoryRepository.save(inventory);
        }
    }
}

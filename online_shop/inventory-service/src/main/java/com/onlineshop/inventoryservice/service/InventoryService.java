package com.onlineshop.inventoryservice.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineshop.inventoryservice.dto.CreateInventoryRequest;
import com.onlineshop.inventoryservice.dto.InventoryResponse;
import com.onlineshop.inventoryservice.dto.OrderItemRequest;
import com.onlineshop.inventoryservice.dto.OrderRequest;
import com.onlineshop.inventoryservice.model.Inventory;
import com.onlineshop.inventoryservice.repository.InventoryRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryService {

    private InventoryRepository inventoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void addNewProductToInventory(CreateInventoryRequest createInventoryRequest) {
        if (inventoryRepository.findByProductId(createInventoryRequest.getProductId()) != null) {
            return;
        }
        Inventory inventory = Inventory.builder().productId(createInventoryRequest.getProductId())
                .quantity(createInventoryRequest.getQuantity()).build();
        inventoryRepository.save(inventory);
    }

    public void substractProductsQuantity(Long orderId, Jwt jwt) {
        JsonNode orderItemsNode = getOrderItemsFromRequestAsJsonNode(orderId, jwt);
        for (JsonNode itemNode : orderItemsNode) {
            Long productId = itemNode.get("productId").asLong();
            Integer quantity = itemNode.get("quantity").asInt();
            Inventory inventory = inventoryRepository.findByProductId(productId);
            if (inventory == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
                        "No inventory found for productId: " + productId);
            }
            if (inventory.getQuantity() - quantity < 0) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                        "Not enough inventory for productId: " + productId);
            }
            inventory.setQuantity(inventory.getQuantity() - quantity);
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

    private JsonNode getOrderItemsFromRequestAsJsonNode(Long orderId, Jwt jwt) {
        String url = "http://ORDER/api/v1/order/" + orderId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
            JsonNode orderItemsNode = jsonNode.get("orderItems");
            if (!orderItemsNode.isArray()) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
            return orderItemsNode;
        } catch (JsonProcessingException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void handleOrderCancelation(OrderRequest orderRequest) {
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            Inventory inventory = inventoryRepository.findByProductId(orderItemRequest.getProductId());
            inventory.setQuantity(inventory.getQuantity() + orderItemRequest.getQuantity());
            inventoryRepository.save(inventory);
        }
    }
}

package com.onlineshop.inventoryservice.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.inventoryservice.dto.CreateInventoryRequest;
import com.onlineshop.inventoryservice.dto.InventoryResponse;
import com.onlineshop.inventoryservice.dto.OrderIdRequest;
import com.onlineshop.inventoryservice.service.InventoryService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResponse> createInventory(
            @Valid @RequestBody CreateInventoryRequest createInventoryRequest) {
        var inventory = inventoryService.addNewProductToInventory(createInventoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateInventoryQuantity(@PathVariable("id") Long id,
            @RequestParam("quantity") Integer quantity) {
        inventoryService.updateInventoryQuantity(id, quantity);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/order-processing")
    public ResponseEntity<?> substractProductsQuantity(@Valid @RequestBody OrderIdRequest orderId) {
        inventoryService.substractProductsQuantity(orderId.getOrderId());
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Set<InventoryResponse>> getAllInventories() {
        var inventories = inventoryService.getAllInventories();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/productIds")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Set<InventoryResponse> getInventoriesByProductIds(@RequestParam("productIds") List<Long> productIds) {
        return inventoryService.getInventoriesByProductIds(productIds);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteInventory(@PathVariable("id") Long inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build();
    }
}

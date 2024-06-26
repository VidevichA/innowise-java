package com.onlineshop.inventoryservice.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.inventoryservice.dto.CreateInventoryRequest;
import com.onlineshop.inventoryservice.dto.InventoryResponse;
import com.onlineshop.inventoryservice.service.InventoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void createInventory(@Valid @RequestBody CreateInventoryRequest createInventoryRequest) {
        inventoryService.addNewProductToInventory(createInventoryRequest);
    }

    @PutMapping("/{id}/{quantity}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateInventoryQuantity(@PathVariable("id") Long id, @PathVariable("quantity") Integer quantity) {
        inventoryService.updateInventoryQuantity(id, quantity);
    }

    @PostMapping("/order-processing")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> substractProductsQuantity(@NotNull @RequestBody Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        inventoryService.substractProductsQuantity(orderId, jwt);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Set<InventoryResponse> getAllInventories() {
        return inventoryService.getAllInventories();
    }

    @GetMapping("/productIds")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Set<InventoryResponse> getInventoriesByProductIds(@RequestParam("productIds") List<Long> productIds) {
        return inventoryService.getInventoriesByProductIds(productIds);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable("id") Long inventoryId) {
        inventoryService.deleteInventory(inventoryId);
    }
}

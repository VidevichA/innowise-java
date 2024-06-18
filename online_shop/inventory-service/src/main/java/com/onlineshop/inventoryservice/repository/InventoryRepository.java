package com.onlineshop.inventoryservice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.onlineshop.inventoryservice.model.Inventory;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    Inventory findByProductId(Long productId);

    List<Inventory> findByProductIdIn(List<Long> productIds);
}

package com.onlineshop.inventoryservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.onlineshop.inventoryservice.model.Inventory;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);

    Set<Optional<Inventory>> findByProductIdIn(List<Long> productIds);
}

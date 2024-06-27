package com.onlineshop.orderservice.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.onlineshop.orderservice.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Set<Optional<Order>> findByUserId(String userId);
}

package com.onlineshop.orderservice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.onlineshop.orderservice.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByUserId(String userId);
}

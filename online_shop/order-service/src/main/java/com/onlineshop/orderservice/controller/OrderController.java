package com.onlineshop.orderservice.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.orderservice.dto.OrderRequest;
import com.onlineshop.orderservice.dto.OrderResponse;
import com.onlineshop.orderservice.model.OrderStatus;
import com.onlineshop.orderservice.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest createOrderDto,
            Authentication auth) {
        OrderResponse order = orderService.createOrder(createOrderDto, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/user/orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Set<OrderResponse>> getOrdersByUserId(Authentication auth) {
        var orders = orderService.getOrdersByUserId(auth.getName());
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> completeOrder(@PathVariable("id") Long orderId, Authentication auth) {
        orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") Long id, Authentication auth) {
        OrderResponse order = orderService.getOrderById(id, auth.getName());
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteOrderById(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<OrderResponse>> getAllOrders() {
        var orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id, Authentication auth) {
        orderService.cancelOrder(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

}

package com.onlineshop.orderservice.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.orderservice.dto.OrderRequest;
import com.onlineshop.orderservice.dto.OrderResponse;
import com.onlineshop.orderservice.model.OrderStatus;
import com.onlineshop.orderservice.service.OrderService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void createOrder(@Valid @RequestBody OrderRequest createOrderDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        orderService.createOrder(createOrderDto, jwt);
    }

    @GetMapping("/user/orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Set<OrderResponse> getOrdersByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return orderService.getOrdersByUserId(jwt.getClaimAsString("sub"));
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void completeOrder(@PathVariable("id") Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        // TODO payment check logic
        orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED, jwt.getClaimAsString("sub"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public OrderResponse getOrderById(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return orderService.getOrderById(id, jwt.getClaimAsString("sub"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrderById(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Set<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void cancelOrder(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        orderService.cancelOrder(id, jwt.getClaimAsString("sub"));
    }

}

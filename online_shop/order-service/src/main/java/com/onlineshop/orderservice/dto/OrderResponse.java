package com.onlineshop.orderservice.dto;

import java.math.BigDecimal;
import java.util.List;

import com.onlineshop.orderservice.model.OrderItem;
import com.onlineshop.orderservice.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String userId;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private BigDecimal totalPrice;
}

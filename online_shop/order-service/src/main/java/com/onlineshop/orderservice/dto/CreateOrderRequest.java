package com.onlineshop.orderservice.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotNull(message = "OrderItems cannot be null")
    @Valid
    private List<CreateOrderItemDto> orderItems;
}

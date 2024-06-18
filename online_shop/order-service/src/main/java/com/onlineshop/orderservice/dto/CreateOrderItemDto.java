package com.onlineshop.orderservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderItemDto {

    @NotNull(message = "ProductId cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
}

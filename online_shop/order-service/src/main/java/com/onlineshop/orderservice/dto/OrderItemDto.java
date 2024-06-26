package com.onlineshop.orderservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    @NotNull(message = "ProductId cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
}

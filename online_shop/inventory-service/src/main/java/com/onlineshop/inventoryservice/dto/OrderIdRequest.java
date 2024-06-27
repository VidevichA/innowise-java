package com.onlineshop.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderIdRequest {

    @NotNull(message = "OrderId cannot be null")
    private Long orderId;
}

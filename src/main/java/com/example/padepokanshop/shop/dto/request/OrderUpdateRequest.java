package com.example.padepokanshop.shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateRequest {
    @NotNull(message = "Please select customer first")
    private Long customerId;

    @NotEmpty(message = "Please select items first")
    private List<OrderItemRequest> items;
}

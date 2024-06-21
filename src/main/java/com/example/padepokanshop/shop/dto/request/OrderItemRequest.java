package com.example.padepokanshop.shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull(message = "Item ca't be empty")
    private Long itemId;

    @NotNull(message = "Quantity can't be empty")
    @Min(value = 1, message = "Quantity must be more than 0")
    private Integer quantity;
}

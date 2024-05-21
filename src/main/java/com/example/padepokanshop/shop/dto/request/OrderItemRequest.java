package com.example.padepokanshop.shop.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long itemId;
    private Integer quantity;
}

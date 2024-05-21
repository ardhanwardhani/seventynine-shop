package com.example.padepokanshop.shop.dto.request;

import lombok.Data;

@Data
public class OrderUpdateRequest {
    private Long customerId;
    private Long itemId;
    private Integer quantity;
}

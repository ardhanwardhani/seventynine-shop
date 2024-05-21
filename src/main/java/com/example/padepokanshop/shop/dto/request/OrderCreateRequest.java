package com.example.padepokanshop.shop.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {
    private Long customerId;
    private List<OrderItemRequest> items;
}

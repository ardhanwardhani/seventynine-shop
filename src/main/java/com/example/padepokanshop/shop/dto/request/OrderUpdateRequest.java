package com.example.padepokanshop.shop.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateRequest {
    private Long customerId;
    private List<OrderItemRequest> orderItems;
}

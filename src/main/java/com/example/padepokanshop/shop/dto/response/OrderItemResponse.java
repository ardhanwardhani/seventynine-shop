package com.example.padepokanshop.shop.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long orderItemId;
    private Long itemId;
    private String itemCode;
    private String itemName;
    private Double price;
    private Integer quantity;
}

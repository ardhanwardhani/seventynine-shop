package com.example.padepokanshop.shop.dto.request;

import lombok.Data;

@Data
public class ItemRequest {
    private String name;
    private String code;
    private Double price;
    private Integer stock;
    private Boolean isAvailable;
}
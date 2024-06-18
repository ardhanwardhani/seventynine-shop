package com.example.padepokanshop.shop.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class ItemResponse {
    private String code;
    private String name;
    private Double price;
    private Integer stock;
    private Date lastRestock;
    private Boolean isAvailable;

    public ItemResponse(String code, String name, Double price, Integer stock, Date lastRestock, Boolean isAvailable){
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.lastRestock = lastRestock;
        this.isAvailable = isAvailable;
    }
}

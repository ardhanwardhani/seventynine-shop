package com.example.padepokanshop.shop.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class ItemResponse {
    private String name;
    private String code;
    private Double price;
    private Integer stock;
    private Date lastRestock;
    private Boolean isAvailable;

    public ItemResponse(String name, String code, Double price, Integer stock, Date lastRestock, Boolean isAvailable){
        this.name = name;
        this.code = code;
        this.price = price;
        this.stock = stock;
        this.lastRestock = lastRestock;
        this.isAvailable = isAvailable;
    }
}

package com.example.padepokanshop.shop.dto.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String name;
    private String phone;
    private String address;
    private String code;
    private String pic;
    private Boolean isActive;
}

package com.example.padepokanshop.shop.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerResponse {
    private String code;
    private String name;
    private String phone;
    private String address;
    private String pic;
    private Boolean isActive;
    private Date lastOrderDate;

    public CustomerResponse(String code, String name, String phone, String address, String pic, Boolean isActive, Date lastOrderDate){
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.pic = pic;
        this.isActive = isActive;
        this.lastOrderDate = lastOrderDate;
    }
}

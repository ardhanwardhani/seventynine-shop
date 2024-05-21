package com.example.padepokanshop.shop.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerResponse {
    private String name;
    private String phone;
    private String address;
    private String code;
    private String pic;
    private Boolean isActive;
    private Date lastOrderDate;

    public CustomerResponse(String name, String phone, String address, String code, String pic, Boolean isActive, Date lastOrderDate){
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.code = code;
        this.pic = pic;
        this.isActive = isActive;
        this.lastOrderDate = lastOrderDate;
    }
}

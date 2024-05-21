package com.example.padepokanshop.shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReport {
    private String order_code;
    private Date order_date;
    private String customer_name;
    private String items_name;
    private Double price;
    private Integer quantity;
    private Double total_price;
}

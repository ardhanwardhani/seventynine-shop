package com.example.padepokanshop.shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReport {
    private Date orderDate;
    private String orderCode;
    private String customerName;
    private Long itemsQuantity;
    private Double totalAmount;
}

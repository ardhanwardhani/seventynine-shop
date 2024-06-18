package com.example.padepokanshop.shop.dto.response;

import com.example.padepokanshop.shop.model.Customer;
import com.example.padepokanshop.shop.model.OrderItem;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderSummary {
    private Long orderId;
    private Date orderDate;
    private String orderCode;
    private String customerName;
    private Integer totalQuantity;
    private Double totalAmount;
}

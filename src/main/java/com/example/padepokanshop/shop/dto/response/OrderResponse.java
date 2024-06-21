package com.example.padepokanshop.shop.dto.response;

import com.example.padepokanshop.shop.model.OrderItem;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {
    private Long orderId;
    private String orderCode;
    private Date orderDate;
    private Long customerId;
    private String customerName;
    private List<OrderItemResponse> orderItems;
}

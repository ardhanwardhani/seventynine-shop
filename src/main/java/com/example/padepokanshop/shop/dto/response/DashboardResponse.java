package com.example.padepokanshop.shop.dto.response;

import lombok.Data;

@Data
public class DashboardResponse {
    private Long countAllOrders;
    private Long countAllCustomers;
    private Long countActiveCustomers;
    private Long countDeactiveCustomers;
    private Long countAllItems;
    private Long countAvailableItems;
    private Long countUnavailableItems;
}

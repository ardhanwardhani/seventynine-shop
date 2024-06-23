package com.example.padepokanshop.shop.service;

import com.example.padepokanshop.shop.dto.response.DashboardResponse;
import com.example.padepokanshop.shop.model.Order;
import com.example.padepokanshop.shop.repository.CustomerRepository;
import com.example.padepokanshop.shop.repository.ItemRepository;
import com.example.padepokanshop.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public DashboardService(CustomerRepository customerRepository, ItemRepository itemRepository, OrderRepository orderRepository){
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    public DashboardResponse getDasboardData(){
        DashboardResponse response = new DashboardResponse();
        response.setCountAllOrders(orderRepository.countAllOrders());
        response.setCountAllCustomers(customerRepository.countAllCustomers());
        response.setCountActiveCustomers(customerRepository.countActiveCustomers());
        response.setCountDeactiveCustomers(customerRepository.countDeactiveCustomers());
        response.setCountAllItems(itemRepository.countAllItems());
        response.setCountAvailableItems(itemRepository.countAvailableItems());
        response.setCountUnavailableItems(itemRepository.countUnavailableItems());
        return response;
    }
}

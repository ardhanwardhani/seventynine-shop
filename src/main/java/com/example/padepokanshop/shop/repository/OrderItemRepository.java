package com.example.padepokanshop.shop.repository;

import com.example.padepokanshop.shop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

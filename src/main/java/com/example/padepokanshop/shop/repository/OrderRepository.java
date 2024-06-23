package com.example.padepokanshop.shop.repository;

import com.example.padepokanshop.shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByOrderDateBetween(
            @Param("startDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date startDate,
            @Param("endDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date endDate);

    List<Order> findOrderByCustomerId(@Param("customerId") Long customerId);

    @Query(value = "SELECT o.order_code FROM orders o WHERE o.order_code LIKE :prefix% ORDER BY o.order_id DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLastOrderCode(@Param("prefix") String prefix);

    @Query("SELECT COUNT(o) FROM Order o")
    Long countAllOrders();
}

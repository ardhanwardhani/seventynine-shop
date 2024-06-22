package com.example.padepokanshop.shop.repository;

import com.example.padepokanshop.shop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "SELECT c.customer_code FROM customers c WHERE c.customer_code LIKE :prefix% ORDER BY c.customer_id DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLastCustomerCode(@Param("prefix") String prefix);

    List<Customer> findByIsActiveTrue();

    List<Customer> findByIsActiveFalse();
}

package com.example.padepokanshop.shop.repository;

import com.example.padepokanshop.shop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

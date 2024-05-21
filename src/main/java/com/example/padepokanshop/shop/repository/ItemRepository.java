package com.example.padepokanshop.shop.repository;

import com.example.padepokanshop.shop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}

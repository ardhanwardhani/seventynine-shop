package com.example.padepokanshop.shop.repository;

import com.example.padepokanshop.shop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "SELECT i.item_code FROM items i WHERE i.item_code LIKE :prefix% ORDER BY i.item_id DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLastItemCode(@Param("prefix") String prefix);
}

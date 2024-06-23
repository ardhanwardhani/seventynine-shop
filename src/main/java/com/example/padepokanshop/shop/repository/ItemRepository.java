package com.example.padepokanshop.shop.repository;

import com.example.padepokanshop.shop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "SELECT i.item_code FROM items i WHERE i.item_code LIKE :prefix% ORDER BY i.item_id DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLastItemCode(@Param("prefix") String prefix);

    List<Item> findByIsAvailableTrue();

    List<Item> findByIsAvailableFalse();

    @Query("SELECT COUNT(i) FROM Item i")
    Long countAllItems();

    @Query("SELECT COUNT(i) FROM Item i WHERE i.isAvailable = true AND i.stock > 0")
    Long countAvailableItems();

    @Query("SELECT COUNT(i) FROM Item i WHERE i.isAvailable = false AND i.stock = 0")
    Long countUnavailableItems();
}

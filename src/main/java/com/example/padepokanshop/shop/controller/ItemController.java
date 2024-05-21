package com.example.padepokanshop.shop.controller;

import com.example.padepokanshop.shop.dto.request.ItemRequest;
import com.example.padepokanshop.shop.dto.response.CustomerResponse;
import com.example.padepokanshop.shop.dto.response.ItemResponse;
import com.example.padepokanshop.shop.model.Item;
import com.example.padepokanshop.shop.service.ItemService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping("/list")
    public ResponseEntity<Object> listItems(){
        try{
            List<Item> items = itemService.getAllItem();
            if (items.isEmpty()){
                return ResponseEntity.ok("No items found");
            }
            return ResponseEntity.ok(items);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching items");
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id){
        try{
            return itemService.getItemById(id).map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest request){
        ItemResponse response = itemService.createItem(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @RequestBody ItemRequest request){
        try{
            ItemResponse response = itemService.updateItems(id, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id){
        Optional<Item> item = itemService.getItemById(id);

        if (item.isEmpty()){
            return ResponseEntity.ok("Failed to delete item");
        }

        itemService.deleteItem(id);
        return ResponseEntity.ok("Deleted items successfully");
    }
}
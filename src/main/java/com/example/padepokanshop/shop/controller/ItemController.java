package com.example.padepokanshop.shop.controller;

import com.example.padepokanshop.shop.dto.request.ItemRequest;
import com.example.padepokanshop.shop.dto.response.ItemResponse;
import com.example.padepokanshop.shop.model.Item;
import com.example.padepokanshop.shop.service.ItemService;
import jakarta.validation.Valid;
import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/item", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/list-items")
    public ResponseEntity<List<Item>> listItems(){
        return ResponseEntity.ok(itemService.getAllItem());
    }

    @GetMapping("/list-available-items")
    public ResponseEntity<Object> getAvailableItems(){
        try{
            List<Item> items = itemService.getAvailableItems();
            if (items.isEmpty()){
                String message = "No items found";
                return ResponseEntity.ok(message);
            }
            return ResponseEntity.ok(items);
        }catch (Exception e){
            String errorMessage = "Error fetching items";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/list-unavailable-items")
    public ResponseEntity<Object> getUnavailableItems(){
        try {
            List<Item> items = itemService.getUnavailableItems();
            if (items.isEmpty()){
                String message = "No items found";
                return ResponseEntity.ok(message);
            }
            return ResponseEntity.ok(items);
        }catch (Exception e){
            String errorMessage = "Error fetching items";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id){
        try{
            return itemService.getItemById(id).map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemRequest request){
        ItemResponse response = itemService.createItem(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@Valid @PathVariable Long id, @RequestBody ItemRequest request){
        try{
            ItemResponse response = itemService.updateItems(id, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/available-item")
    public ResponseEntity<Map<String, String>> availableItem(@PathVariable Long id){
        Optional<Item> optionalItem = itemService.getItemById(id);

        if (optionalItem.isEmpty()){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Item with ID " + id + " is not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        itemService.availableItem(id);
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Item activated successfully");
        return ResponseEntity.ok(successResponse);
    }

    @PatchMapping("/{id}/unavailable-item")
    public ResponseEntity<Map<String, String>> unavailableItem(@PathVariable Long id){
        Optional<Item> optionalItem = itemService.getItemById(id);

        if (optionalItem.isEmpty()){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Item with ID " + id + " is not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        itemService.unavailableItem(id);
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Item unavailable succesfully");
        return ResponseEntity.ok(successResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id){
        Optional<Item> item = itemService.getItemById(id);

        if (item.isEmpty()){
            return ResponseEntity.ok("Failed to delete item");
        }

        itemService.deleteItem(id);
        return ResponseEntity.ok("Deleted items successfully");
    }
}

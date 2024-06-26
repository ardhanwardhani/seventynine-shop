package com.example.padepokanshop.shop.service;

import com.example.padepokanshop.shop.dto.request.ItemRequest;
import com.example.padepokanshop.shop.dto.response.ItemResponse;
import com.example.padepokanshop.shop.model.Item;
import com.example.padepokanshop.shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItem(){
        return itemRepository.findAll();
    }

    public List<Item> getAvailableItems(){
        return itemRepository.findByIsAvailableTrue();
    }

    public List<Item> getUnavailableItems(){
        return itemRepository.findByIsAvailableFalse();
    }

    public Optional<Item> getItemById(Long id){
        return Optional.ofNullable(itemRepository.findById(id).orElseThrow(
                ()
                        -> new RuntimeException("Item with ID" + id + "not found")
        ));
    }

    public ItemResponse createItem(ItemRequest request){
        Item item = new Item();
        item.setName(request.getName());
        item.setCode(generateItemCode());
        item.setPrice(request.getPrice());
        item.setStock(request.getStock());
        item.setIsAvailable(true);

        Item savedItem = itemRepository.save(item);
        return new ItemResponse(
                savedItem.getName(),
                savedItem.getCode(),
                savedItem.getPrice(),
                savedItem.getStock(),
                savedItem.getLastRestock(),
                savedItem.getIsAvailable()
        );
    }

    public ItemResponse updateItems(Long id, ItemRequest request){
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isPresent()){
            Item existingItem = optionalItem.get();
            existingItem.setName(request.getName());
            existingItem.setPrice(request.getPrice());
            existingItem.setStock(request.getStock());
            existingItem.setIsAvailable(request.getIsAvailable());

            Item updatedItem = itemRepository.save(existingItem);

            return new ItemResponse(
                    updatedItem.getName(),
                    updatedItem.getCode(),
                    updatedItem.getPrice(),
                    updatedItem.getStock(),
                    updatedItem.getLastRestock(),
                    updatedItem.getIsAvailable()
            );
        }else {
            throw new RuntimeException("Item with ID" + id + "not found");
        }
    }

    public void availableItem(Long id){
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isEmpty()){
            throw new RuntimeException("Item with ID " + id + " is not found");
        }

        Item item = optionalItem.get();
        item.setIsAvailable(true);
        itemRepository.save(item);
    }

    public void unavailableItem(Long id){
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isEmpty()){
            throw new RuntimeException("Item with ID " + id + " is not found");
        }

        Item item = optionalItem.get();
        item.setIsAvailable(false);
        itemRepository.save(item);
    }

    public void deleteItem(Long id){
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()){
            itemRepository.deleteById(id);
        }else {
            throw new RuntimeException("Item with ID" + id + "not found");
        }
    }

    private String generateItemCode() {
        String prefix = "I-";
        Optional<String> lastCodeOptional = itemRepository.findLastItemCode(prefix);

        int newCodeNumber = 1;
        if (lastCodeOptional.isPresent()) {
            String lastCode = lastCodeOptional.get();
            String[] parts = lastCode.split("-");
            if (parts.length == 2) {
                try {
                    newCodeNumber = Integer.parseInt(parts[1]) + 1;
                } catch (NumberFormatException e) {
                    // Handle error appropriately
                }
            }
        }

        return prefix + String.format("%05d", newCodeNumber);
    }

}

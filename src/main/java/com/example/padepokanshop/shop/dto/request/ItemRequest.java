package com.example.padepokanshop.shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequest {
    @NotBlank(message = "Item Name can't be empty")
    private String name;

    @NotNull(message = "Item Price can't be empty")
    @Min(value = 1, message = "The price must be more than 1")
    private Double price;

    @NotNull(message = "Item Stock can't be empty")
    @Min(value = 1, message = "Item stock must be more than 0")
    private Integer stock;

    @NotNull(message = "Availability can't be empty")
    private Boolean isAvailable;
}

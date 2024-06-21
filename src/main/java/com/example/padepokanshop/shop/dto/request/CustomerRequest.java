package com.example.padepokanshop.shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 5, max = 255, message = "Address must have at least 5 to 255 characters")
    private String address;

    private String imageUrl;

    @NotNull(message = "User Status can't be empty")
    private Boolean isActive;
}

package com.fooddelivery.dto.response;

import lombok.Data;

@Data
public class MenuResponseDto {
    private Long itemId;
    private String name;
    private String description;
    private double price;
    private Long restaurantId;
    private boolean isAvailable;
}
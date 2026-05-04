package com.fooddelivery.dto.request;

import lombok.Data;

@Data
public class MenuRequestDto {
    private String name;
    private String description;
    private double price;
    private Long restaurantId;
}
package com.fooddelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    private Long itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private Long restaurantId;
    private boolean isAvailable;
    private int quantity;

}
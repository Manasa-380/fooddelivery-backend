package com.fooddelivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    private Long restaurantId;
    private Long userId;
    private String restaurantName;
    private String location;
    private String contactNumber;
}
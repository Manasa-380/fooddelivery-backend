package com.fooddelivery.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    private Long restaurantId;
    private Long userId;
    private String restaurantName;
    private String location;
    private String contactNumber;
}

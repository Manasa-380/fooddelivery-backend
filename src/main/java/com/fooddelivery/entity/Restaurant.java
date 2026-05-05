package com.fooddelivery.entity;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
=======
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
    private Long restaurantId;
    private Long userId;
    private String restaurantName;
    private String location;
    private String contactNumber;
}
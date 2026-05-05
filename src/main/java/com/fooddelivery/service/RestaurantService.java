package com.fooddelivery.service;

import com.fooddelivery.entity.Restaurant;
<<<<<<< HEAD
=======

>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
import java.util.List;

public interface RestaurantService {
    void registerRestaurant(Restaurant restaurant);
<<<<<<< HEAD
=======
    Restaurant getByUserId(Long userId);
>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
    Restaurant getRestaurant(Long restaurantId);
    List<Restaurant> getAllRestaurants();
    void updateRestaurant(Long restaurantId, Restaurant restaurant);
    void deleteRestaurant(Long restaurantId);
}
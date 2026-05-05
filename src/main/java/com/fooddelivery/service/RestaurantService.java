package com.fooddelivery.service;

import com.fooddelivery.entity.Restaurant;
import java.util.List;

public interface RestaurantService {
    void registerRestaurant(Restaurant restaurant);
    Restaurant getByUserId(Long userId);
    Restaurant getRestaurant(Long restaurantId);
    List<Restaurant> getAllRestaurants();
    void updateRestaurant(Long restaurantId, Restaurant restaurant);
    void deleteRestaurant(Long restaurantId);

}
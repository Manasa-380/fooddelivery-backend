package com.service;

import com.entity.Restaurant;
import java.util.List;

public interface RestaurantService {
    void registerRestaurant(Restaurant restaurant);
    Restaurant getRestaurant(Long restaurantId);
    List<Restaurant> getAllRestaurants();
    void updateRestaurant(Long restaurantId, Restaurant restaurant);
    void deleteRestaurant(Long restaurantId);
}
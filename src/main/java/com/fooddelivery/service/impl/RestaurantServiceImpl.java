package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public void registerRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    // ✅ THIS WAS MISSING — REQUIRED BY INTERFACE
    @Override
    public Restaurant getByUserId(Long userId) {
        Restaurant restaurant = restaurantRepository.findById(userId);
        if (restaurant == null) {
            throw new ResourceNotFoundException(
                    "Restaurant not found for user ID: " + userId);
        }
        return restaurant;
    }

    @Override
    public Restaurant getRestaurant(Long restaurantId) {
        Restaurant r = restaurantRepository.findById(restaurantId);
        if (r == null) {
            throw new ResourceNotFoundException(
                    "Restaurant not found with ID: " + restaurantId);
        }
        return r;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public void updateRestaurant(Long restaurantId, Restaurant restaurant) {
        restaurant.setRestaurantId(restaurantId);
        restaurantRepository.update(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.delete(restaurantId);
    }
}

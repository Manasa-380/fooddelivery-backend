package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void registerRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant getByUserId(Long userId) {
        try {
            return restaurantRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Restaurant not found for user");
        }
    }

    @Override
    public Restaurant getRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
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
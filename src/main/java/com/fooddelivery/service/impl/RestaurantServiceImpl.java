package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Restaurant;
<<<<<<< HEAD
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
=======
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.RestaurantService;
import org.springframework.stereotype.Service;

>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

<<<<<<< HEAD
    @Autowired
    private RestaurantRepository restaurantRepository;
=======
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9

    @Override
    public void registerRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    @Override
<<<<<<< HEAD
=======
    public Restaurant getByUserId(Long userId) {
        try {
            return restaurantRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Restaurant not found for user");
        }
    }

    @Override
>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
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
<<<<<<< HEAD
}
=======
}
>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9

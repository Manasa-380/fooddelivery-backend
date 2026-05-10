package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service implementation for Restaurant operations.
 * Contains business logic for registering, updating,
 * deleting and retrieving restaurants.
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Registers a new restaurant in the database.
     * Called when a new RESTAURANT_OWNER completes registration.
     *
     * @param restaurant the Restaurant object with owner's restaurant details
     */
    @Override
    public void registerRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    /**
     * Retrieves a single restaurant by its ID.
     *
     * @param restaurantId the ID of the restaurant to fetch
     * @return the Restaurant object matching the given ID
     */
    @Override
    public Restaurant getRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    /**
     * Retrieves all restaurants from the database.
     * Displayed when restaurant owner selects View All Restaurants.
     *
     * @return list of all Restaurant objects
     */
    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    /**
     * Updates an existing restaurant's details.
     * Sets the restaurantId on the object before passing to repository
     * so the correct row gets updated in the database.
     *
     * @param restaurantId the ID of the restaurant to update
     * @param restaurant   the Restaurant object with new details
     */
    @Override
    public void updateRestaurant(Long restaurantId, Restaurant restaurant) {
        restaurant.setRestaurantId(restaurantId);
        restaurantRepository.update(restaurant);
    }

    /**
     * Permanently deletes a restaurant from the database.
     * After deletion the restaurant owner is automatically logged out.
     *
     * @param restaurantId the ID of the restaurant to delete
     */
    @Override
    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.delete(restaurantId);
    }
}
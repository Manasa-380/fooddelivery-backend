package com.fooddelivery.repository;

import com.fooddelivery.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository class for Restaurant.
 * Handles all database operations for the restaurants table
 * using JdbcTemplate.
 */
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * RowMapper that converts each database row into a Restaurant object.
     * Maps column names from restaurants table to Restaurant fields.
     */
    private RowMapper<Restaurant> rowMapper = new RowMapper<>() {
        public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
            Restaurant r = new Restaurant();
            r.setRestaurantId(rs.getLong("restaurant_id"));
            r.setUserId(rs.getLong("user_id"));
            r.setRestaurantName(rs.getString("restaurant_name"));
            r.setLocation(rs.getString("location"));
            r.setContactNumber(rs.getString("contact_number"));
            return r;
        }
    };

    /**
     * Saves a new restaurant to the database.
     * Called during RESTAURANT_OWNER registration.
     *
     * @param restaurant the Restaurant object containing details to save
     */
    public void save(Restaurant restaurant) {
        String sql = "INSERT INTO restaurants (user_id, restaurant_name, location, contact_number) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql,
                restaurant.getUserId(),
                restaurant.getRestaurantName(),
                restaurant.getLocation(),
                restaurant.getContactNumber());
        System.out.println("Restaurant saved successfully!");
    }

    /**
     * Finds a single restaurant by its ID.
     *
     * @param restaurantId the ID of the restaurant to find
     * @return the Restaurant object matching the given ID
     */
    public Restaurant findById(Long restaurantId) {
        String sql = "SELECT * FROM restaurants WHERE restaurant_id=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, restaurantId);
    }

    /**
     * Retrieves all restaurants from the database.
     * Used when restaurant owner selects "View All Restaurants".
     *
     * @return list of all Restaurant objects
     */
    public List<Restaurant> findAll() {
        String sql = "SELECT * FROM restaurants";
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * Updates an existing restaurant's name, location and contact number.
     *
     * @param restaurant the Restaurant object with updated details and restaurantId
     */
    public void update(Restaurant restaurant) {
        String sql = "UPDATE restaurants SET restaurant_name=?, location=?, contact_number=? WHERE restaurant_id=?";
        jdbcTemplate.update(sql,
                restaurant.getRestaurantName(),
                restaurant.getLocation(),
                restaurant.getContactNumber(),
                restaurant.getRestaurantId());
        System.out.println("Restaurant updated successfully!");
    }

    /**
     * Permanently deletes a restaurant from the database.
     *
     * @param restaurantId the ID of the restaurant to delete
     */
    public void delete(Long restaurantId) {
        String sql = "DELETE FROM restaurants WHERE restaurant_id=?";
        jdbcTemplate.update(sql, restaurantId);
        System.out.println("Restaurant deleted successfully!");
    }

    /**
     * Finds a restaurant ID by its name.
     * Used during order placement when customer types restaurant name.
     *
     * @param restaurantName the name of the restaurant
     * @return the restaurant ID as Long
     */
    public Long findRestaurantIdByName(String restaurantName) {
        return jdbcTemplate.queryForObject(
                "SELECT restaurant_id FROM restaurants WHERE restaurant_name = ?",
                Long.class,
                restaurantName
        );
    }

    /**
     * Finds a restaurant by the user ID of its owner.
     * Used after login to load the restaurant owner's restaurant details.
     * Returns null if no restaurant found for that user.
     *
     * @param userId the user ID of the restaurant owner
     * @return the Restaurant object or null if not found
     */
    public Restaurant findByUserId(Long userId) {
        String sql = "SELECT * FROM restaurants WHERE user_id = ?";
        List<Restaurant> list = jdbcTemplate.query(sql, rowMapper, userId);
        return list.isEmpty() ? null : list.get(0);
    }
}
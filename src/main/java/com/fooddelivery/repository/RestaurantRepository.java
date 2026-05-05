package com.fooddelivery.repository;

import com.fooddelivery.entity.Restaurant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RestaurantRepository {

    private final JdbcTemplate jdbcTemplate;

    public RestaurantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Restaurant> rowMapper =
            (rs, rowNum) -> new Restaurant(
                    rs.getLong("restaurant_id"),
                    rs.getLong("user_id"),
                    rs.getString("restaurant_name"),
                    rs.getString("location"),
                    rs.getString("contact_number")
            );

    public void save(Restaurant restaurant) {
        String sql = """
                INSERT INTO restaurants
                (user_id, restaurant_name, location, contact_number)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                restaurant.getUserId(),
                restaurant.getRestaurantName(),
                restaurant.getLocation(),
                restaurant.getContactNumber()
        );
        System.out.println("Restaurant saved successfully!");
    }

    public Restaurant findByUserId(Long userId) {
        String sql = "SELECT * FROM restaurants WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }
    public Restaurant findById(Long restaurantId) {
        String sql = "SELECT * FROM restaurants WHERE restaurant_id=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, restaurantId);
    }

    public List<Restaurant> findAll() {
        String sql = "SELECT * FROM restaurants";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void update(Restaurant restaurant) {
        String sql = "UPDATE restaurants SET restaurant_name=?, location=?, contact_number=? WHERE restaurant_id=?";
        jdbcTemplate.update(sql,
                restaurant.getRestaurantName(),
                restaurant.getLocation(),
                restaurant.getContactNumber(),
                restaurant.getRestaurantId());
        System.out.println("Restaurant updated successfully!");
    }

    public void delete(Long restaurantId) {
        String sql = "DELETE FROM restaurants WHERE restaurant_id=?";
        jdbcTemplate.update(sql, restaurantId);
        System.out.println("Restaurant deleted successfully!");
    }
    // ✅ Lookup restaurant_id by restaurant_name
    public Long findRestaurantIdByName(String restaurantName) {
        return jdbcTemplate.queryForObject(
                "SELECT restaurant_id FROM restaurants WHERE restaurant_name = ?",
                Long.class,
                restaurantName
        );
    }
}



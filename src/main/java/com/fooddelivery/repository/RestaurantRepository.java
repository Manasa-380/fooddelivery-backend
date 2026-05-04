package com.fooddelivery.repository;

import com.fooddelivery.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    public void save(Restaurant restaurant) {
        String sql = "INSERT INTO restaurants (user_id, restaurant_name, location, contact_number) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql,
                restaurant.getUserId(),
                restaurant.getRestaurantName(),
                restaurant.getLocation(),
                restaurant.getContactNumber());
        System.out.println("Restaurant saved successfully!");
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
}
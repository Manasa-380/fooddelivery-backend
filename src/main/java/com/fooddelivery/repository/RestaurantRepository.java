package com.fooddelivery.repository;

import com.fooddelivery.entity.Restaurant;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
=======
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
import java.util.List;

@Repository
public class RestaurantRepository {

<<<<<<< HEAD
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
=======
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

>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
        jdbcTemplate.update(sql,
                restaurant.getUserId(),
                restaurant.getRestaurantName(),
                restaurant.getLocation(),
<<<<<<< HEAD
                restaurant.getContactNumber());
        System.out.println("Restaurant saved successfully!");
    }

=======
                restaurant.getContactNumber()
        );
        System.out.println("Restaurant saved successfully!");
    }

    public Restaurant findByUserId(Long userId) {
        String sql = "SELECT * FROM restaurants WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }
>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
    public Restaurant findById(Long restaurantId) {
        String sql = "SELECT * FROM restaurants WHERE restaurant_id=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, restaurantId);
    }
<<<<<<< HEAD
=======

>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
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
<<<<<<< HEAD


=======
>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
    // ✅ Lookup restaurant_id by restaurant_name
    public Long findRestaurantIdByName(String restaurantName) {
        return jdbcTemplate.queryForObject(
                "SELECT restaurant_id FROM restaurants WHERE restaurant_name = ?",
                Long.class,
                restaurantName
        );
    }
<<<<<<< HEAD

}
=======
}


>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9

package com.fooddelivery.repository;

import com.fooddelivery.entity.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MenuItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<MenuItem> rowMapper = new RowMapper<>() {
        public MenuItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            MenuItem item = new MenuItem();
            item.setItemId(rs.getLong("item_id"));
            item.setName(rs.getString("item_name"));
            item.setDescription(rs.getString("description"));
            item.setPrice(rs.getBigDecimal("price"));
            item.setRestaurantId(rs.getLong("restaurant_id"));
            item.setAvailable(rs.getBoolean("is_available"));
            return item;
        }
    };

    public void save(MenuItem item) {
        String sql = "INSERT INTO menu_items (item_name, description, price, restaurant_id, is_available) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getRestaurantId(),
                true);
        System.out.println("Menu item saved successfully!");
    }

    public void update(MenuItem item) {
        String sql = "UPDATE menu_items SET item_name=?, description=?, price=? WHERE item_id=?";
        jdbcTemplate.update(sql,
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getItemId());
        System.out.println("Menu item updated successfully!");
    }

    public void delete(Long itemId) {
        String sql = "DELETE FROM menu_items WHERE item_id=?";
        jdbcTemplate.update(sql, itemId);
        System.out.println("Menu item deleted successfully!");
    }

    public MenuItem findById(Long itemId) {
        String sql = "SELECT * FROM menu_items WHERE item_id=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, itemId);
    }

    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        String sql = "SELECT * FROM menu_items WHERE restaurant_id=?";
        return jdbcTemplate.query(sql, rowMapper, restaurantId);
    }

    public List<MenuItem> findAllAvailable() {
        String sql = "SELECT * FROM menu_items WHERE is_available=true";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void updateAvailability(Long itemId, boolean status) {
        String sql = "UPDATE menu_items SET is_available=? WHERE item_id=?";
        jdbcTemplate.update(sql, status, itemId);
        System.out.println("Availability updated successfully!");
    }
}
package com.fooddelivery.repository;

import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.exception.MenuItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository class for MenuItem.
 * Handles all database operations for the menu_items table
 * using JdbcTemplate.
 */
@Repository
public class MenuItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * RowMapper that converts each database row into a MenuItem object.
     * Maps column names from menu_items table to MenuItem fields.
     */
    private RowMapper<MenuItem> rowMapper = new RowMapper<>() {
        public MenuItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            MenuItem item = new MenuItem();
            item.setItemId(rs.getLong("item_id"));
            item.setName(rs.getString("item_name"));
            item.setDescription(rs.getString("description"));
            item.setPrice(rs.getBigDecimal("price"));
            item.setRestaurantId(rs.getLong("restaurant_id"));
            item.setAvailable(rs.getBoolean("is_available"));
            item.setQuantity(rs.getInt("quantity"));
            return item;
        }
    };

    /**
     * Saves a new menu item to the database.
     * Sets is_available to true by default when a new item is added.
     *
     * @param item the MenuItem object containing item details to save
     */
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

    /**
     * Updates an existing menu item's name, description and price.
     *
     * @param item the MenuItem object containing updated details and itemId
     */
    public void update(MenuItem item) {
        String sql = "UPDATE menu_items SET item_name=?, description=?, price=? WHERE item_id=?";
        jdbcTemplate.update(sql,
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getItemId());
        System.out.println("Menu item updated successfully!");
    }

    /**
     * Permanently deletes a menu item from the database.
     *
     * @param itemId the ID of the menu item to delete
     */
    public void delete(Long itemId) {
        String sql = "DELETE FROM menu_items WHERE item_id=?";
        jdbcTemplate.update(sql, itemId);
        System.out.println("Menu item deleted successfully!");
    }

    /**
     * Finds a single menu item by its ID.
     *
     * @param itemId the ID of the menu item to find
     * @return the MenuItem object matching the given ID
     */
    public MenuItem findById(Long itemId) {
        try {
            String sql = "SELECT * FROM menu_items WHERE item_id=?";
            return jdbcTemplate.queryForObject(sql, rowMapper, itemId);
        } catch (Exception e) {
            throw new MenuItemNotFoundException(
                    "Menu item not found with ID: " + itemId
            );
        }
    }


    /**
     * Finds all menu items belonging to a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return list of all MenuItem objects for that restaurant
     */
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        String sql = "SELECT * FROM menu_items WHERE restaurant_id=?";
        return jdbcTemplate.query(sql, rowMapper, restaurantId);
    }

    /**
     * Finds all menu items that are currently available and in stock.
     * Used by customers to view available items across all restaurants.
     *
     * @return list of MenuItem objects where is_available=1 AND quantity greater than 0
     */
    public List<MenuItem> findAllAvailable() {
        String sql = "SELECT * FROM menu_items WHERE is_available = 1 AND quantity > 0";
        List<MenuItem> items = jdbcTemplate.query(sql, rowMapper);
        if (items.isEmpty()) {
            throw new MenuItemNotFoundException(
                    "No available menu items found."
            );
        }
        return items;
    }

    /**
     * Updates the availability status of a menu item.
     * Restaurant owner uses this to toggle item ON or OFF.
     *
     * @param itemId the ID of the menu item
     * @param status true to make available, false to make unavailable
     */
    public void updateAvailability(Long itemId, boolean status) {
        String sql = "UPDATE menu_items SET is_available=? WHERE item_id=?";
        jdbcTemplate.update(sql, status, itemId);
        System.out.println("Availability updated successfully!");
    }

    /**
     * Finds a menu item by its name within a specific restaurant.
     * Used during order placement when customer types item name.
     *
     * @param itemName     the name of the menu item
     * @param restaurantId the ID of the restaurant
     * @return the matching MenuItem object
     */
    /**
     * Finds a menu item by name within a restaurant.
     * Throws MenuItemNotFoundException if item does not exist.
     */
    public MenuItem findMenuItemByName(String itemName, Long restaurantId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT item_id AS itemId, item_name AS name, price, quantity " +
                            "FROM menu_items WHERE item_name = ? AND restaurant_id = ?",
                    new BeanPropertyRowMapper<>(MenuItem.class),
                    itemName,
                    restaurantId
            );
        } catch (Exception e) {
            throw new MenuItemNotFoundException(
                    "Menu item '" + itemName + "' not found in this restaurant."
            );
        }
    }

    /**
     * Finds all available and in-stock items for a specific restaurant.
     * Used to display menu when customer selects a restaurant to order from.
     *
     * @param restaurantId the ID of the restaurant
     * @return list of available MenuItem objects for that restaurant
     */
    public List<MenuItem> findAllAvailableItemsByRestaurant(Long restaurantId) {
        return jdbcTemplate.query(
                "SELECT item_id AS itemId, item_name AS name, price, quantity " +
                        "FROM menu_items " +
                        "WHERE restaurant_id = ? AND is_available = TRUE AND quantity > 0",
                new BeanPropertyRowMapper<>(MenuItem.class),
                restaurantId
        );
    }

    /**
     * Reduces the stock quantity of a menu item after an order is placed.
     * Automatically marks item as unavailable if quantity reaches zero.
     *
     * @param itemId     the ID of the menu item
     * @param orderedQty the quantity ordered by the customer
     */
    public void reduceQuantity(Long itemId, int orderedQty) {
        jdbcTemplate.update(
                "UPDATE menu_items " +
                        "SET quantity = quantity - ?, " +
                        "    is_available = CASE WHEN quantity - ? <= 0 THEN FALSE ELSE TRUE END " +
                        "WHERE item_id = ? AND quantity >= ?",
                orderedQty,
                orderedQty,
                itemId,
                orderedQty
        );
    }
}
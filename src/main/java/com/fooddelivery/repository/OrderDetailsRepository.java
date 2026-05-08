package com.fooddelivery.repository;

import com.fooddelivery.entity.OrderDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class responsible for database operations related to order items.
 *
 * <p>This repository interacts with the {@code order_items} table and handles
 * persistence and retrieval of individual menu items that belong to an order.</p>
 */
@Repository
public class OrderDetailsRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderDetailsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a single order item into the {@code order_items} table.
     *
     * @param details OrderDetails object containing order item information
     * @throws DataAccessException if database error occurs
     */
    public void save(OrderDetails details) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO order_items (order_id, item_id, quantity, price) VALUES (?, ?, ?, ?)",
                    details.getOrderId(),
                    details.getItemId(),
                    details.getQuantity(),
                    details.getPrice()
            );
        } catch (DataAccessException ex) {
            throw ex; // Propagate to service for transaction rollback
        }
    }

    /**
     * Retrieves all order items associated with a given order ID.
     *
     * @param orderId the unique ID of the order
     * @return list of OrderDetails objects (empty list if no items found)
     * @throws DataAccessException if database error occurs
     */
    public List<OrderDetails> findByOrderId(Long orderId) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM order_items WHERE order_id = ?",
                    new BeanPropertyRowMapper<>(OrderDetails.class),
                    orderId
            );
        } catch (DataAccessException ex) {
            throw ex; // Let service layer handle rollback
        }
    }
}
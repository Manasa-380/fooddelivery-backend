package com.repository;

import com.entity.OrderDetails;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDetailsRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor injection
    public OrderDetailsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save order item (order_items table)
    public void save(OrderDetails details) {
        jdbcTemplate.update(
                "INSERT INTO order_items (order_id, item_id, quantity, price) VALUES (?, ?, ?, ?)",
                details.getOrderId(),
                details.getItemId(),
                details.getQuantity(),
                details.getPrice()
        );
    }

    // Fetch all items for an order
    public List<OrderDetails> findByOrderId(Long orderId) {
        return jdbcTemplate.query(
                "SELECT * FROM order_items WHERE order_id = ?",
                new BeanPropertyRowMapper<>(OrderDetails.class),
                orderId
        );
    }
}
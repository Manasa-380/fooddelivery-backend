package com.fooddelivery.repository;

import com.fooddelivery.entity.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor injection
    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ Save order and return generated orderId
    public Long save(Order order) {

        String sql = """
            INSERT INTO orders
            (customer_id, restaurant_id, order_status, total_amount)
            VALUES (?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, order.getCustomerId());
            ps.setLong(2, order.getRestaurantId());
            ps.setString(3, order.getOrderStatus());
            ps.setBigDecimal(4, order.getTotalAmount());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    // Find order by ID
    public Order findById(Long orderId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM orders WHERE order_id = ?",
                new BeanPropertyRowMapper<>(Order.class),
                orderId
        );
    }

    // Find orders by customer
    public List<Order> findByCustomerId(Long customerId) {
        return jdbcTemplate.query(
                "SELECT * FROM orders WHERE customer_id = ?",
                new BeanPropertyRowMapper<>(Order.class),
                customerId
        );
    }

    // Update order status
    public void updateStatus(Long orderId, String status) {
        jdbcTemplate.update(
                "UPDATE orders SET order_status = ? WHERE order_id = ?",
                status,
                orderId
        );
    }
}

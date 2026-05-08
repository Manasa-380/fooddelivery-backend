package com.fooddelivery.repository;

import com.fooddelivery.entity.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Repository class for handling database operations related to orders.
 *
 * <p>Uses JdbcTemplate to perform CRUD operations on the
 * {@code orders} table.</p>
 */
@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new order into the database and returns the generated order ID.
     *
     * @param order Order entity containing order details
     * @return generated order ID
     */
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

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId order ID
     * @return Order entity if found, otherwise null
     */
    public Order findById(Long orderId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM orders WHERE order_id = ?",
                    new BeanPropertyRowMapper<>(Order.class),
                    orderId
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Retrieves all orders placed by a customer.
     *
     * @param customerId customer ID
     * @return list of orders
     */
    public List<Order> findByCustomerId(Long customerId) {
        return jdbcTemplate.query(
                "SELECT * FROM orders WHERE customer_id = ?",
                new BeanPropertyRowMapper<>(Order.class),
                customerId
        );
    }

    /**
     * Updates order status.
     *
     * @param orderId order ID
     * @param status new status
     */
    public void updateStatus(Long orderId, String status) {
        jdbcTemplate.update(
                "UPDATE orders SET order_status = ? WHERE order_id = ?",
                status,
                orderId
        );
    }
}

package com.fooddelivery.repository;

import com.fooddelivery.entity.Delivery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static org.springframework.boot.env.RandomValuePropertySourceEnvironmentPostProcessor.ORDER;

@Repository
public class DeliveryRepository {

    private final JdbcTemplate jdbcTemplate;

    public DeliveryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Delivery delivery) {
        String sql = """
            INSERT INTO deliveries (order_id, agent_id, delivery_status, eta)
                                       VALUES (?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                delivery.getOrderId(),
                delivery.getAgentId(),
                delivery.getStatus(),
                delivery.getEta()

        );
    }

    public Delivery findByOrderId(Long orderId) {
        String sql = "SELECT * FROM deliveries WHERE order_id = ? ORDER BY delivery_id DESC LIMIT 1;";

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Delivery d = new Delivery();
                d.setDeliveryId(rs.getLong("delivery_id"));
                d.setOrderId(rs.getLong("order_id"));
                d.setAgentId(rs.getLong("agent_id"));
                d.setStatus(rs.getString("delivery_status"));
                d.setEta(rs.getTimestamp("eta").toLocalDateTime());
                return d;
            }
            return null;
        }, orderId);
    }

    public void updateStatus(Long deliveryId, String delivery_status) {
        String sql = "UPDATE deliveries SET delivery_status = ? WHERE delivery_id = ?";
        jdbcTemplate.update(sql, delivery_status, deliveryId);
    }
}

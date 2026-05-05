package com.fooddelivery.repository;

import com.fooddelivery.entity.Delivery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
        String sql = "SELECT * FROM deliveries WHERE order_id = ?";

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Delivery d = new Delivery();
                d.setDeliveryId(rs.getLong("delivery_id"));
                d.setOrderId(rs.getLong("order_id"));
                d.setAgentId(rs.getLong("agent_id"));
                d.setStatus(rs.getString("agent_status"));
                d.setEta(rs.getTimestamp("eta").toLocalDateTime());
                return d;
            }
            return null;
        }, orderId);
    }

    public void updateStatus(Long deliveryId, String status) {
        String sql = "UPDATE delivery SET status = ? WHERE delivery_id = ?";
        jdbcTemplate.update(sql, status, deliveryId);
    }
}

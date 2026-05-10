package com.fooddelivery.repository;

import com.fooddelivery.entity.Delivery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Repository class for Delivery.
 * Handles all database operations for the deliveries table
 * using JdbcTemplate.
 */
@Repository
public class DeliveryRepository {
    private static final Logger log =
            LoggerFactory.getLogger(DeliveryRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public DeliveryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new delivery record into the database.
     *
     * SQL:
     * INSERT INTO deliveries (order_id, agent_id, delivery_status, eta)
     * VALUES (?, ?, ?, ?)
     *
     * @param delivery the Delivery object containing order and agent details
     */
    public void save(Delivery delivery) {
        try {
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

            log.info("Delivery record saved successfully!");

        } catch (Exception e) {
            log.info("Error while saving delivery:{} " ,e.getMessage());
            throw e;
        }
    }

    /**
     * Finds the most recent delivery for a given order ID.
     *
     * SQL:
     * SELECT * FROM deliveries
     * WHERE order_id = ?
     * ORDER BY delivery_id DESC
     * LIMIT 1
     *
     * @param orderId the order ID
     * @return latest Delivery object or null if not found
     */
    public Delivery findByOrderId(Long orderId) {

        Delivery delivery = null;

        try {
            String sql = "SELECT * FROM deliveries WHERE order_id = ? ORDER BY delivery_id DESC LIMIT 1";

            delivery = jdbcTemplate.query(sql, rs -> {
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

        } catch (Exception e) {
            log.info("Error fetching delivery by orderId: {} " , e.getMessage());
        }

        return delivery;
    }

    /**
     * Updates delivery status for a specific delivery ID.
     *
     * SQL:
     * UPDATE deliveries
     * SET delivery_status = ?
     * WHERE delivery_id = ?
     *
     * @param deliveryId the delivery ID
     * @param deliveryStatus new status (e.g., Delivered, In Transit)
     */
    public void updateStatus(Long deliveryId, String deliveryStatus) {

        try {
            String sql;

            if ("DELIVERED".equalsIgnoreCase(deliveryStatus)) {
                sql = "UPDATE deliveries SET delivery_status = ?, delivered_at = CURRENT_TIMESTAMP WHERE delivery_id = ?";
                jdbcTemplate.update(sql, deliveryStatus, deliveryId);
            }
            else {
                sql = "UPDATE deliveries set delivery_status=? where delivery_id=?";


                jdbcTemplate.update(sql, deliveryStatus, deliveryId);
            }

            log.info("Delivery status updated successfully!");

        } catch (Exception e) {
            log.info("Error updating delivery status: {} " , e.getMessage());
            throw e;
        }
    }

    /**
     * Finds all deliveries handled by a particular delivery agent.
     *
     * SQL:
     * SELECT * FROM deliveries
     * WHERE agent_id = ?
     *
     * @param agentId the agent ID
     * @return list of Delivery objects handled by the agent
     */
    public List<Delivery> findByAgentId(Long agentId) {

        List<Delivery> deliveries = null;

        try {
            String sql = "SELECT * FROM deliveries WHERE agent_id = ?";

            deliveries = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Delivery d = new Delivery();
                d.setDeliveryId(rs.getLong("delivery_id"));
                d.setOrderId(rs.getLong("order_id"));
                d.setAgentId(rs.getLong("agent_id"));
                d.setStatus(rs.getString("delivery_status"));
                d.setEta(rs.getTimestamp("eta").toLocalDateTime());
                return d;
            }, agentId);

        } catch (Exception e) {
            log.info("Error fetching deliveries by agentId: {} " , e.getMessage());
        }

        return deliveries;
    }
}
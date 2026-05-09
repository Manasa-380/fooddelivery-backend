package com.fooddelivery.repository;
import com.fooddelivery.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Payment payment) {
        String sql = """
            INSERT INTO payments (order_id, payment_method, amount, payment_status)
            VALUES (?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                payment.getOrderId(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getPaymentStatus()
        );
    }

    // ✅ SAFE METHOD (NO CRASH)
    public Payment findByOrderId(Long orderId) {

        String sql = "SELECT * FROM payments WHERE order_id = ?";

        List<Payment> payments = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Payment.class),
                orderId
        );

        return payments.isEmpty() ? null : payments.get(0);
    }
}
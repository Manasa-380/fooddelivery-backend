package com.fooddelivery.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ Lookup customer_id by customer_name
    public Long findCustomerIdByName(String customerName) {
        return jdbcTemplate.queryForObject(
                "SELECT customer_id FROM customers WHERE customer_name = ?",
                Long.class,
                customerName
        );
    }
}




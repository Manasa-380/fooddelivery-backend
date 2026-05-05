package com.fooddelivery.repository;
import com.fooddelivery.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Customer customer) {

        String sql = """
            INSERT INTO customers (user_id, customer_name, phone, address)
            VALUES (?, ?, ?, ?)
        """;

        return jdbcTemplate.update(
                sql,
                customer.getUserId(),
                customer.getCustomerName(),
                customer.getPhone(),
                customer.getAddress()
        );
    }

    public Customer findByUserId(Long userId) {
        String sql = "SELECT * FROM customers WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), userId);
    }

    public Customer findById(Long customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), customerId);
    }

    public int update(Customer customer) {

        String sql = """
            UPDATE customers
            SET customer_name = ?, phone = ?, address = ?
            WHERE customer_id = ?
        """;

        return jdbcTemplate.update(
                sql,
                customer.getCustomerName(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getCustomerId()
        );
    }
    // ✅ Lookup customer_id by customer_name
    public Long findCustomerIdByName(String customerName) {
        return jdbcTemplate.queryForObject(
                "SELECT customer_id FROM customers WHERE customer_name = ?",
                Long.class,
                customerName
        );
    }
    private static class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Customer(
                    rs.getLong("customer_id"),
                    rs.getLong("user_id"),
                    rs.getString("customer_name"),
                    rs.getString("phone"),
                    rs.getString("address")
            );
        }
    }
}
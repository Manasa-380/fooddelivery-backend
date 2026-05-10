package com.fooddelivery.repository;

import com.fooddelivery.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository class for performing CRUD operations on the {@code customers} table.
 */
@Repository
public class CustomerRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomerRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Inserts a new customer record into the database.
     *
     * @param customer the {@link Customer} entity to persist
     * @return number of rows affected
     */
    public int save(Customer customer) {
        log.debug("Saving customer: {}", customer.getCustomerName());
        String sql = """
            INSERT INTO customers (user_id, customer_name, phone, address)
            VALUES (?, ?, ?, ?)
        """;
        int rows = jdbcTemplate.update(sql,
                customer.getUserId(),
                customer.getCustomerName(),
                customer.getPhone(),
                customer.getAddress());
        log.info("Customer saved: {}", customer.getCustomerName());
        return rows;
    }

    /**
     * Finds a customer by their associated user ID.
     *
     * @param userId the user ID linked to the customer
     * @return the matching {@link Customer}
     */
    public Customer findByUserId(Long userId) {
        log.debug("Finding customer by userId: {}", userId);
        String sql = "SELECT * FROM customers WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), userId);
    }

    /**
     * Finds a customer by their customer ID.
     *
     * @param customerId the customer's primary key
     * @return the matching {@link Customer}
     */
    public Customer findById(Long customerId) {
        log.debug("Finding customer by customerId: {}", customerId);
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), customerId);
    }

    /**
     * Updates an existing customer's profile information.
     *
     * @param customer the {@link Customer} with updated fields
     * @return number of rows affected
     */
    public int update(Customer customer) {
        log.debug("Updating customer ID: {}", customer.getCustomerId());
        String sql = """
            UPDATE customers
            SET customer_name = ?, phone = ?, address = ?
            WHERE customer_id = ?
        """;
        int rows = jdbcTemplate.update(sql,
                customer.getCustomerName(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getCustomerId());
        log.info("Customer updated, ID: {}", customer.getCustomerId());
        return rows;
    }

    /**
     * Looks up a customer's ID by their name.
     *
     * @param customerName the name to search for
     * @return the matching customer ID
     */
    public Long findCustomerIdByName(String customerName) {
        log.debug("Looking up customer ID by name: {}", customerName);
        return jdbcTemplate.queryForObject(
                "SELECT customer_id FROM customers WHERE customer_name = ?",
                Long.class,
                customerName
        );
    }

    /** Maps a ResultSet row to a {@link Customer} object. */
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
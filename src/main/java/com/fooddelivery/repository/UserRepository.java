package com.fooddelivery.repository;

import com.fooddelivery.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository class for performing CRUD operations on the {@code users} table.
 */
@Repository
public class UserRepository {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Inserts a new user record into the database.
     *
     * @param user the {@link User} entity to persist
     * @return number of rows affected
     */
    public int save(User user) {
        log.debug("Saving user with email: {}", user.getEmail());
        String sql = """
            INSERT INTO users (email, password, role, status)
            VALUES (?, ?, ?, ?)
        """;
        int rows = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getStatus());
        log.info("User saved successfully with email: {}", user.getEmail());
        return rows;
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email to search for
     * @return the matching {@link User}
     */
    public User findByEmail(String email) {
        log.debug("Looking up user by email: {}", email);
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
    }

    /** Maps a ResultSet row to a {@link User} object. */
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getLong("user_id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("status")
            );
        }
    }
}
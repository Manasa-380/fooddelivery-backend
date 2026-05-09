package com.repository;
import com.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(User user) {
        String sql = """
            INSERT INTO users (email, password, role, status)
            VALUES (?, ?, ?, ?)
        """;

        return jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getStatus()
        );
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
    }

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
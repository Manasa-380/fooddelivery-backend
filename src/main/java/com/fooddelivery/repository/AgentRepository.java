package com.fooddelivery.repository;

import com.fooddelivery.entity.Agent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AgentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AgentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Agent> AGENT_ROW_MAPPER =
            (rs, rowNum) -> new Agent(
                    rs.getLong("agent_id"),
                    rs.getLong("user_id"),
                    rs.getString("agent_name"),
                    rs.getString("contact_number"),
                    rs.getString("agent_status")
            );

    public void save(Agent agent) {
        String sql = """
            INSERT INTO agents (user_id, agent_name, contact_number, agent_status)
            VALUES (?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
                agent.getUserId(),
                agent.getAgentName(),
                agent.getContactNumber(),
                agent.getAgentStatus()
        );
    }

    public Agent findAvailableAgent() {
        String sql = "SELECT * FROM agents WHERE agent_status = 'AVAILABLE' LIMIT 1";
        return jdbcTemplate.query(sql, AGENT_ROW_MAPPER)
                .stream().findFirst().orElse(null);
    }

    public Agent findByUserId(Long userId) {
        String sql = "SELECT * FROM agents WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, AGENT_ROW_MAPPER, userId);
    }

    public void updateAgentStatus(Long agentId, String status) {
        String sql = "UPDATE agents SET agent_status = ? WHERE agent_id = ?";
        jdbcTemplate.update(sql, status, agentId);
    }
}

package com.fooddelivery.repository;

import com.fooddelivery.entity.Agent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
                INSERT INTO agents
                (user_id, agent_name, contact_number, agent_status)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                agent.getUserId(),
                agent.getAgentName(),
                agent.getContactNumber(),
                agent.getAgentStatus()
        );
    }

    //------------
    public Optional<Agent> findFirstByAgentStatus(String status) {

        String sql = """
        SELECT * FROM agents
        WHERE agent_status = ?
        LIMIT 1
    """;

        return jdbcTemplate
                .query(sql, AGENT_ROW_MAPPER, status)
                .stream()
                .findFirst();
    }

    //--------------------

    public Agent findByUserId(Long userId) {
        String sql = "SELECT * FROM agents WHERE user_id = ?";
        return jdbcTemplate.query(sql, AGENT_ROW_MAPPER, userId)
                .stream().findFirst().orElse(null);
    }

    public Agent findByAgentId(Long agentId) {
        String sql = "SELECT * FROM agents WHERE agent_id = ?";
        return jdbcTemplate.query(sql, AGENT_ROW_MAPPER, agentId)
                .stream().findFirst().orElse(null);
    }

    public void updateAgentStatus(Long agentId, String status) {
        String sql = "UPDATE agents SET agent_status = ? WHERE agent_id = ?";
        jdbcTemplate.update(sql, status, agentId);
    }
}

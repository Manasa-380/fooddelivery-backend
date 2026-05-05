package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Agent;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.AgentRepository;
import com.fooddelivery.service.DeliveryService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final AgentRepository agentRepository;

    public DeliveryServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public void createAgent(Agent agent) {
        if (agent.getAgentStatus() == null) {
            agent.setAgentStatus("AVAILABLE");
        }
        agentRepository.save(agent);
    }

    @Override
    public Agent getAgentByUserId(Long userId) {
        try {
            return agentRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Agent not found for user");
        }
    }
}
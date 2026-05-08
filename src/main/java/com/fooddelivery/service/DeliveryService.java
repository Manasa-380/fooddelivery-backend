package com.fooddelivery.service;

import com.fooddelivery.entity.Agent;

public interface DeliveryService {

    void createAgent(Agent agent);

    Agent getAgentByUserId(Long userId);
}
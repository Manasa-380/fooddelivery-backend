package com.fooddelivery.service;
import com.fooddelivery.dto.request.RegisterRequestDto;
import com.fooddelivery.entity.Agent;
import com.fooddelivery.dto.response.DeliveryResponseDto;

public interface DeliveryService {

    //  For login to work (User + Agent creation)
   // void registerAgent(RegisterRequestDto dto);

    //  Pure delivery domain operations
    //void createAgent(Agent agent);
    Agent getAgentByUserId(Long userId);

    void assignDelivery(Long orderId);
    DeliveryResponseDto getDeliveryByOrderId(Long orderId);
    void updateDeliveryStatus(Long deliveryId, String status);

    String getDeliveryStatusByOrderId(Long orderId);
}

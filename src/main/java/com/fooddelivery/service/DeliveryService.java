package com.fooddelivery.service;

import com.fooddelivery.dto.request.DeliveryRequestDto;
import com.fooddelivery.dto.response.DeliveryResponseDto;
import com.fooddelivery.entity.Agent;

public interface DeliveryService {
    void createAgent(Agent agent);
    Agent getAgentByUserId(Long userId);

    DeliveryResponseDto assignDelivery(DeliveryRequestDto deliveryRequestDto);

    DeliveryResponseDto getDeliveryByOrderId(Long orderId);
    void processDeliveryAfterPayment(Long orderId, String customerName);
    void updateDeliveryStatus(Long deliveryId, String status);

    String getDeliveryStatusByOrderId(Long orderId);
}
package com.service;

import com.dto.request.DeliveryRequestDto;
import com.dto.response.DeliveryResponseDto;
import com.entity.Agent;

import java.util.List;
public interface DeliveryService {
    void createAgent(Agent agent);
    Agent getAgentByUserId(Long userId);

    DeliveryResponseDto assignDelivery(DeliveryRequestDto deliveryRequestDto);

    DeliveryResponseDto getDeliveryByOrderId(Long orderId);
    void processDeliveryAfterPayment(Long orderId, String customerName);
    void updateDeliveryStatus(Long deliveryId, String status);

    String getDeliveryStatusByOrderId(Long orderId);


    List<DeliveryResponseDto> getDeliveriesByAgent(Long agentId);

}
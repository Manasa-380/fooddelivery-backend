package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.DeliveryRequestDto;
import com.fooddelivery.dto.response.DeliveryResponseDto;
import com.fooddelivery.entity.Agent;
import com.fooddelivery.entity.Delivery;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.AgentRepository;
import com.fooddelivery.repository.DeliveryRepository;
import com.fooddelivery.service.DeliveryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final AgentRepository agentRepository;
    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceImpl(AgentRepository agentRepository,
                               DeliveryRepository deliveryRepository) {
        this.agentRepository = agentRepository;
        this.deliveryRepository = deliveryRepository;
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

        Agent agent = agentRepository.findByUserId(userId);
        if (agent == null) {
            throw new ResourceNotFoundException("Agent not found for user");
        }
        return agent;
    }
    @Override
    public DeliveryResponseDto assignDelivery(DeliveryRequestDto dto) {

        Agent agent = agentRepository.findByAgentId(dto.getAgentId());
        if (agent == null) {
            throw new ResourceNotFoundException("Agent not found");
        }

        if (!"AVAILABLE".equals(agent.getAgentStatus())) {
            throw new ResourceNotFoundException("Agent is not available for delivery");
        }

        Delivery delivery = new Delivery();
        delivery.setOrderId(dto.getOrderId());
        delivery.setAgentId(agent.getAgentId());
        delivery.setStatus("ASSIGNED");
        delivery.setEta(
                ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
                        .plusSeconds(30)
                        .toLocalDateTime()
        );
        // ✅ save (void)
        deliveryRepository.save(delivery);

        agentRepository.updateAgentStatus(agent.getAgentId(), "BUSY");
        System.out.println("Agent set to BUSY");

        // ✅ fetch saved delivery
        Delivery savedDelivery = deliveryRepository.findByOrderId(dto.getOrderId());

        return new DeliveryResponseDto(
                savedDelivery.getDeliveryId(),
                savedDelivery.getStatus(),
                savedDelivery.getEta()
        );
    }
    @Override
    public DeliveryResponseDto getDeliveryByOrderId(Long orderId) {

        Delivery delivery = deliveryRepository.findByOrderId(orderId);
        if (delivery == null) {
            throw new ResourceNotFoundException("Delivery not found for order");
        }

        return new DeliveryResponseDto(
                delivery.getDeliveryId(),
                delivery.getStatus(),
                delivery.getEta()
        );
    }

    @Override
    public void updateDeliveryStatus(Long deliveryId, String status) {
        deliveryRepository.updateStatus(deliveryId, status);
    }

    @Override
    public String getDeliveryStatusByOrderId(Long orderId) {

        Delivery delivery = deliveryRepository.findByOrderId(orderId);
        if (delivery == null) {
            throw new ResourceNotFoundException("Delivery not found for order");
        }

        return delivery.getStatus();
    }




    @Override
    public void processDeliveryAfterPayment(Long orderId, String customerName) {

        // 1️⃣ Find available agent
        Agent agent = agentRepository
                .findFirstByAgentStatus("AVAILABLE")
                .orElse(null);

        if (agent == null) {
            System.out.println(
                    "🚫 Delivery partners are busy, it may take a moment to deliver your order."
            );
            return;
        }

        // 2️⃣ Assign delivery
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setAgentId(agent.getAgentId());
        delivery.setStatus("ASSIGNED");
        delivery.setEta(LocalDateTime.now().plusSeconds(90)); // ✅ seconds

       // deliveryRepository.save(delivery);
        deliveryRepository.save(delivery);

// ✅ fetch actual saved delivery
        Delivery savedDelivery = deliveryRepository.findByOrderId(orderId);
        Long deliveryId = savedDelivery.getDeliveryId();



        agentRepository.updateAgentStatus(agent.getAgentId(), "BUSY");

        System.out.println("🔵 Agent now BUSY");

        System.out.println(
                "✅ Your delivery will be taken care by "
                        + agent.getAgentName()
                        + " and ETA is " + delivery.getEta()
        );

        // 3️⃣ Order preparation (10 seconds)
        waitSeconds(10, "⏳ Preparing your order...");

        deliveryRepository.updateStatus(deliveryId, "PICKED_UP");
       // deliveryRepository.updateStatus(delivery.getDeliveryId(), "PICKED_UP");

        System.out.println(
                "📦 Your order has been picked up by " + agent.getAgentName()
        );

        // 4️⃣ Delivery (20 seconds)
        waitSeconds(60, "🚚 Out for delivery...");
        deliveryRepository.updateStatus(deliveryId, "DELIVERED");
        agentRepository.updateAgentStatus(agent.getAgentId(), "AVAILABLE");

        System.out.println(
                "🎉 Dear " + customerName +
                        ", your order has been delivered successfully!"
        );

    }
    private void waitSeconds(int seconds, String message) {
        try {
            System.out.println(message);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
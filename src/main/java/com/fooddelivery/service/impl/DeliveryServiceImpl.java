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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private static final Logger log =
            LoggerFactory.getLogger(DeliveryServiceImpl.class);
    private final AgentRepository agentRepository;
    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceImpl(AgentRepository agentRepository,
                               DeliveryRepository deliveryRepository) {
        this.agentRepository = agentRepository;
        this.deliveryRepository = deliveryRepository;
    }

    // ================= AGENT =================

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

    // ================= DELIVERY =================

    @Override
    public DeliveryResponseDto assignDelivery(DeliveryRequestDto dto) {

        Agent agent = agentRepository.findByAgentId(dto.getAgentId());
        if (agent == null) {
            throw new ResourceNotFoundException("Agent not found");
        }

        if (!"AVAILABLE".equals(agent.getAgentStatus())) {
            throw new ResourceNotFoundException("Agent is not available");
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

        deliveryRepository.save(delivery);

        agentRepository.updateAgentStatus(agent.getAgentId(), "BUSY");

        return new DeliveryResponseDto(
                delivery.getDeliveryId(),
                delivery.getOrderId(),
                delivery.getStatus(),
                delivery.getEta()
        );
    }

    @Override
    public DeliveryResponseDto getDeliveryByOrderId(Long orderId) {

        Delivery delivery = deliveryRepository.findByOrderId(orderId);
        if (delivery == null) {
            throw new ResourceNotFoundException("Delivery not found");
        }

        return new DeliveryResponseDto(
                delivery.getDeliveryId(),
                delivery.getOrderId(),
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
            throw new ResourceNotFoundException("Delivery not found");
        }
        return delivery.getStatus();
    }

    @Override
    public List<DeliveryResponseDto> getDeliveriesByAgent(Long agentId) {

        List<Delivery> deliveries = deliveryRepository.findByAgentId(agentId);
        List<DeliveryResponseDto> result = new ArrayList<>();

        for (Delivery d : deliveries) {
            result.add(new DeliveryResponseDto(
                    d.getDeliveryId(),
                    d.getOrderId(),
                    d.getStatus(),
                    d.getEta()
            ));
        }

        return result;
    }

    // ================= MAIN DELIVERY FLOW =================

    @Override
    public void processDeliveryAfterPayment(Long orderId, String customerName) {
        Agent agent = null;
        // 1️⃣ Find available agent
        while(agent==null) {
           agent = agentRepository
                    .findFirstByAgentStatus("AVAILABLE")
                    .orElse(null);

            if (agent == null) {

                log.info("⏳ All delivery partners busy... retrying in 5 seconds...");
                waitSeconds(5);

            }
        }

        String agentName = agent.getAgentName();

        // 2️⃣ Create delivery
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setAgentId(agent.getAgentId());
        delivery.setStatus("ASSIGNED");
        delivery.setEta(LocalDateTime.now().plusSeconds(90));

        deliveryRepository.save(delivery);

        Delivery savedDelivery = deliveryRepository.findByOrderId(orderId);
        Long deliveryId = savedDelivery.getDeliveryId();

        agentRepository.updateAgentStatus(agent.getAgentId(), "BUSY");

        // ✅ DELIVERY UI FLOW

        log.info("\n🚚 Order Confirmed!");
        waitSeconds(2);

        log.info("✅ Assigned to {}" , agentName);
        waitSeconds(2);

        log.info("🕒 ETA: {}" , formatTime(delivery.getEta()));
        waitSeconds(2);

        showProgress(10);
        waitSeconds(3);

        // Preparing
        printDeliveryStep("Preparing Food", "Restaurant is cooking your order 🍳");
        showProgress(40);
        waitSeconds(4);

        // Picked Up
        deliveryRepository.updateStatus(deliveryId, "PICKED_UP");
        printDeliveryStep("Picked Up", agentName + " picked up your order 📦");
        showProgress(70);
        waitSeconds(4);

        // On the way
        deliveryRepository.updateStatus(deliveryId, "ON_THE_WAY");
        printDeliveryStep("On The Way", agentName + " is on the way 🚚");
        showProgress(90);
        waitSeconds(4);

        // Delivered
        deliveryRepository.updateStatus(deliveryId, "DELIVERED");
        agentRepository.updateAgentStatus(agent.getAgentId(), "AVAILABLE");

        printDeliveryStep("Delivered",
                "🎉 Dear " + customerName + ", your order has been delivered successfully!");
        showProgress(100);
    }

    // ================= HELPER METHODS =================

    protected void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String formatTime(LocalDateTime time) {
        return time.format(
                java.time.format.DateTimeFormatter.ofPattern("hh:mm a")
        );
    }

    private void showProgress(int percent) {
        System.out.print("[");
        int bars = percent / 10;
        for (int i = 0; i < 10; i++) {
            if (i < bars) System.out.print("█");
            else System.out.print(" ");
        }
        System.out.println("] " + percent + "%");
    }

    private void printDeliveryStep(String step, String message) {
        log.info("\n📍 {}" , step);
        log.info(" {}  " , message);
    }
}
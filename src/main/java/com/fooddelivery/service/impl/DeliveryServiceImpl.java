package com.fooddelivery.service.impl;

//import com.fooddelivery.dto.request.RegisterRequestDto;
import com.fooddelivery.dto.response.DeliveryResponseDto;
import com.fooddelivery.entity.Agent;
import com.fooddelivery.entity.Delivery;
//import com.fooddelivery.entity.User;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.AgentRepository;
import com.fooddelivery.repository.DeliveryRepository;
//import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.service.DeliveryService;
//import com.fooddelivery.service.OrderService;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final AgentRepository agentRepository;
    private final DeliveryRepository deliveryRepository;
    //private final UserRepository userRepository;
    //private final OrderService orderService;
   // private final PasswordEncoder passwordEncoder;

    public DeliveryServiceImpl(AgentRepository agentRepository,
                               DeliveryRepository deliveryRepository){
                              // UserRepository userRepository,
                              // OrderService orderService){
                               //PasswordEncoder passwordEncoder) {
        this.agentRepository = agentRepository;
        this.deliveryRepository = deliveryRepository;
       // this.userRepository = userRepository;
      //  this.orderService = orderService;
       // this.passwordEncoder = passwordEncoder;
    }
/*
    // ✅ 1️⃣ Agent registration (User + Agent creation)
    @Override
    public void registerAgent(RegisterRequestDto dto) {

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("AGENT");

        userRepository.save(user);

        Agent agent = new Agent();
        agent.setUserId(user.getUserId());
        agent.setAgentName(dto.getName());
        agent.setAddress(dto.getAddress());
        agent.setAgentStatus("AVAILABLE");

        agentRepository.save(agent);
    }
*/
    // ✅ 2️⃣ Get agent using logged-in userId
    @Override
    public Agent getAgentByUserId(Long userId) {

        Agent agent = agentRepository.findByUserId(userId);
        if (agent == null) {
            throw new ResourceNotFoundException("Agent not found for user");
        }
        return agent;
    }

    // ✅ 3️⃣ Assign delivery automatically
    @Override
    public void assignDelivery(Long orderId) {

        Agent agent = agentRepository.findAvailableAgent();
        if (agent == null) {
            throw new ResourceNotFoundException("No available delivery agent");
        }

        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setAgentId(agent.getAgentId());
        delivery.setStatus("ASSIGNED");        delivery.setEta(LocalDateTime.now().plusMinutes(30));

        deliveryRepository.save(delivery);

        agentRepository.updateAgentStatus(agent.getAgentId(), "BUSY");
        //orderService.updateOrderStatus(orderId, "OUT_FOR_DELIVERY");
    }

    // ✅ 4️⃣ Get delivery details
    @Override
    public DeliveryResponseDto getDeliveryByOrderId(Long orderId) {

        Delivery delivery = deliveryRepository.findByOrderId(orderId);
        if (delivery == null) {
            throw new ResourceNotFoundException("Delivery not found");
        }

        return new DeliveryResponseDto(
                delivery.getDeliveryId(),
                delivery.getStatus(),
                delivery.getEta()
        );
    }

    // ✅ 5️⃣ Update delivery status
    @Override
    public void updateDeliveryStatus(Long deliveryId, String status) {

        deliveryRepository.updateStatus(deliveryId, status);
    }

    // ✅ 6️⃣ Get delivery status only
    @Override
    public String getDeliveryStatusByOrderId(Long orderId) {

        Delivery delivery = deliveryRepository.findByOrderId(orderId);
        if (delivery == null) {
            throw new ResourceNotFoundException("Delivery not found");
        }

        return delivery.getStatus();
    }
}
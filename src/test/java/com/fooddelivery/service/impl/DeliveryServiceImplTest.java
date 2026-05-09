package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Agent;
import com.fooddelivery.entity.Delivery;
import com.fooddelivery.repository.AgentRepository;
import com.fooddelivery.repository.DeliveryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DeliveryServiceImplTest {

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    // ✅ TEST 1: Happy Path
    @Test
    void testProcessDelivery_Success() {

        Agent agent = new Agent();
        agent.setAgentId(1L);
        agent.setAgentName("Ravi");
        agent.setAgentStatus("AVAILABLE");

        Delivery savedDelivery = new Delivery();
        savedDelivery.setDeliveryId(100L);
        savedDelivery.setOrderId(1L);
        savedDelivery.setEta(LocalDateTime.now());

        when(agentRepository.findFirstByAgentStatus("AVAILABLE"))
                .thenReturn(Optional.of(agent));

        when(deliveryRepository.findByOrderId(1L))
                .thenReturn(savedDelivery);

        DeliveryServiceImpl spyService = spy(deliveryService);
        doNothing().when(spyService).waitSeconds(anyInt());

        spyService.processDeliveryAfterPayment(1L, "Manasa");

        verify(deliveryRepository).save(any(Delivery.class));
        verify(agentRepository).updateAgentStatus(1L, "BUSY");

        verify(deliveryRepository).updateStatus(100L, "PICKED_UP");
        verify(deliveryRepository).updateStatus(100L, "ON_THE_WAY");
        verify(deliveryRepository).updateStatus(100L, "DELIVERED");

        verify(agentRepository).updateAgentStatus(1L, "AVAILABLE");
    }

    // ✅ TEST 2: Retry then success
    @Test
    void testProcessDelivery_RetryThenSuccess() {

        Agent agent = new Agent();
        agent.setAgentId(2L);
        agent.setAgentName("Ajay");

        Delivery savedDelivery = new Delivery();
        savedDelivery.setDeliveryId(200L);

        when(agentRepository.findFirstByAgentStatus("AVAILABLE"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(agent));

        when(deliveryRepository.findByOrderId(2L))
                .thenReturn(savedDelivery);

        DeliveryServiceImpl spyService = spy(deliveryService);
        doNothing().when(spyService).waitSeconds(anyInt());

        spyService.processDeliveryAfterPayment(2L, "Manasa");

        verify(agentRepository, atLeast(2))
                .findFirstByAgentStatus("AVAILABLE");

        verify(agentRepository).updateAgentStatus(2L, "BUSY");
    }

    // ✅ TEST 3: No agent available (simulate retry loop)
    @Test
    void testProcessDelivery_NoAgentAvailable() {

        when(agentRepository.findFirstByAgentStatus("AVAILABLE"))
                .thenReturn(Optional.empty());

        DeliveryServiceImpl spyService = spy(deliveryService);
        doNothing().when(spyService).waitSeconds(anyInt());

        Thread thread = new Thread(() -> {
            spyService.processDeliveryAfterPayment(3L, "Manasa");
        });

        thread.start();

        try {
            Thread.sleep(2000);
            thread.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verify(agentRepository, atLeast(1))
                .findFirstByAgentStatus("AVAILABLE");
    }

    // ✅ TEST 4: Delivery null edge case
    @Test
    void testProcessDelivery_DeliveryNull() {

        Agent agent = new Agent();
        agent.setAgentId(3L);
        agent.setAgentName("Rahul");

        when(agentRepository.findFirstByAgentStatus("AVAILABLE"))
                .thenReturn(Optional.of(agent));

        when(deliveryRepository.findByOrderId(3L))
                .thenReturn(null);

        DeliveryServiceImpl spyService = spy(deliveryService);
        doNothing().when(spyService).waitSeconds(anyInt());

        // should not crash
        spyService.processDeliveryAfterPayment(3L, "Manasa");

        verify(deliveryRepository).save(any());
    }
}

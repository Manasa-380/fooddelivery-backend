package com.fooddelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    private Long deliveryId;
    private Long orderId;
    private Long agentId;
    private String status;      // OUT_FOR_DELIVERY, DELIVERED
    private LocalDateTime eta;
}
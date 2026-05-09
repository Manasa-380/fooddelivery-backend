package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private String orderStatus;  // PLACED, ACCEPTED, PREPARING, OUT_FOR_DELIVERY, DELIVERED
    private BigDecimal totalAmount;
    private LocalDateTime orderTime;
}
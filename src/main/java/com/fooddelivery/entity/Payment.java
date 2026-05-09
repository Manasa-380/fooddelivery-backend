package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private Long paymentId;
    private Long orderId;
    private String paymentMethod;
    private double amount;
    private String paymentStatus;
}
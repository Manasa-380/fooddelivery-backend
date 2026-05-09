package com.dto.request;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private Long orderId;
    private String paymentMethod;
    private double amount;
}
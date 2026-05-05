package com.fooddelivery.service;

import com.fooddelivery.dto.request.PaymentRequestDto;
import com.fooddelivery.dto.response.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto processPayment(PaymentRequestDto dto);
    PaymentResponseDto getPaymentByOrderId(Long orderId);
    boolean isPaymentSuccessful(Long orderId);
}
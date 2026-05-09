package com.service.impl;

import com.dto.request.PaymentRequestDto;
import com.dto.response.PaymentResponseDto;
import com.entity.Payment;
import com.repository.PaymentRepository;
import com.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto dto) {
        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setAmount(dto.getAmount());

        String method = dto.getPaymentMethod().toUpperCase();
        if (method.equals("CARD") || method.equals("WALLET")) {
            payment.setPaymentStatus("SUCCESS");
        } else {
            payment.setPaymentStatus("FAILED");
        }

        paymentRepository.save(payment);

        return new PaymentResponseDto(
                null,
                payment.getOrderId(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                "Payment processed successfully"
        );
    }

    @Override
    public PaymentResponseDto getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        return new PaymentResponseDto(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                "Payment fetched successfully"
        );
    }

    @Override
    public boolean isPaymentSuccessful(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        return "SUCCESS".equals(payment.getPaymentStatus());
    }
}
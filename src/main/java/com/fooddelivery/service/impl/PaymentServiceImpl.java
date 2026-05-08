package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.PaymentRequestDto;
import com.fooddelivery.dto.response.PaymentResponseDto;
import com.fooddelivery.entity.Payment;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.exception.PaymentNotFoundException;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.PaymentRepository;
import com.fooddelivery.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fooddelivery.entity.Order;
import com.fooddelivery.repository.OrderRepository;
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;




    /**
     * Processes a payment for a given order.
     * Only CARD and WALLET are valid payment methods.
     * Throws InvalidRequestException if input is invalid.
     *
     * @param dto contains orderId, paymentMethod and amount
     * @return PaymentResponseDto with status SUCCESS or FAILED
     */
    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto dto) {
        // ✅ validate orderId
        if (dto.getOrderId() == null || dto.getOrderId() <= 0) {
            throw new InvalidRequestException("Invalid order ID.");
        }
        // ✅ validate payment method
        if (dto.getPaymentMethod() == null || dto.getPaymentMethod().trim().isEmpty()) {
            throw new InvalidRequestException("Payment method cannot be empty.");
        }
        // ✅ validate amount
        if (dto.getAmount() <= 0) {
            throw new InvalidRequestException("Amount must be greater than zero.");
        }

        // ✅ fetch actual order and compare amount
        Order order = orderRepository.findById(dto.getOrderId());
        if (order == null) {
            throw new InvalidRequestException(
                    "Order not found with ID: " + dto.getOrderId()
            );
        }

// ✅ compare using BigDecimal — because totalAmount is BigDecimal in Order
        if (java.math.BigDecimal.valueOf(dto.getAmount())
                .compareTo(order.getTotalAmount()) != 0) {
            throw new InvalidRequestException(
                    "Payment amount Rs." + dto.getAmount() +
                            " does not match order amount Rs." + order.getTotalAmount() +
                            ". Please enter exact amount."
            );
        }

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

    /**
     * Retrieves payment details by order ID.
     * Throws InvalidRequestException if orderId is invalid.
     * Throws PaymentNotFoundException if no payment found for that order.
     *
     * @param orderId the ID of the order
     * @return PaymentResponseDto with payment details
     */
    @Override
    public PaymentResponseDto getPaymentByOrderId(Long orderId) {
        // ✅ validate orderId
        if (orderId == null || orderId <= 0) {
            throw new InvalidRequestException("Invalid order ID.");
        }

        Payment payment = paymentRepository.findByOrderId(orderId);

        // ✅ check if payment exists
        if (payment == null) {
            throw new PaymentNotFoundException(
                    "No payment found for order ID: " + orderId
            );
        }

        return new PaymentResponseDto(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                "Payment fetched successfully"
        );
    }

    /**
     * Checks if payment for an order was successful.
     * Throws InvalidRequestException if orderId is invalid.
     * Throws PaymentNotFoundException if no payment found for that order.
     *
     * @param orderId the ID of the order
     * @return true if payment status is SUCCESS, false otherwise
     */
    @Override
    public boolean isPaymentSuccessful(Long orderId) {
        // ✅ validate orderId
        if (orderId == null || orderId <= 0) {
            throw new InvalidRequestException("Invalid order ID.");
        }

        Payment payment = paymentRepository.findByOrderId(orderId);

        // ✅ check if payment exists
        if (payment == null) {
            throw new PaymentNotFoundException(
                    "No payment found for order ID: " + orderId
            );
        }

        return "SUCCESS".equals(payment.getPaymentStatus());
    }
}
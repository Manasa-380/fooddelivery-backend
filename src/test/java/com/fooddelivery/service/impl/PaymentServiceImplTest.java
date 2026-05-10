
package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.PaymentRequestDto;
import com.fooddelivery.dto.response.PaymentResponseDto;
import com.fooddelivery.entity.Payment;
import com.fooddelivery.repository.PaymentRepository;
import com.fooddelivery.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    // fake repository — no real DB needed

    @InjectMocks
    private PaymentServiceImpl paymentService;
    // real service with fake repository injected

    private PaymentRequestDto cardRequest;
    private PaymentRequestDto walletRequest;
    private PaymentRequestDto invalidRequest;
    private Payment successPayment;
    private Payment failedPayment;

    @BeforeEach
    void setUp() {
        // CARD payment request
        cardRequest = new PaymentRequestDto();
        cardRequest.setOrderId(1L);
        cardRequest.setPaymentMethod("CARD");
        cardRequest.setAmount(250.00);

        // WALLET payment request
        walletRequest = new PaymentRequestDto();
        walletRequest.setOrderId(2L);
        walletRequest.setPaymentMethod("WALLET");
        walletRequest.setAmount(500.00);

        // INVALID payment method request
        invalidRequest = new PaymentRequestDto();
        invalidRequest.setOrderId(3L);
        invalidRequest.setPaymentMethod("CASH");
        invalidRequest.setAmount(100.00);

        // SUCCESS payment — returned by fake repo
        successPayment = new Payment();
        successPayment.setPaymentId(1L);
        successPayment.setOrderId(1L);
        successPayment.setPaymentMethod("CARD");
        successPayment.setAmount(250.00);
        successPayment.setPaymentStatus("SUCCESS");

        // FAILED payment — returned by fake repo
        failedPayment = new Payment();
        failedPayment.setPaymentId(2L);
        failedPayment.setOrderId(3L);
        failedPayment.setPaymentMethod("CASH");
        failedPayment.setAmount(100.00);
        failedPayment.setPaymentStatus("FAILED");
    }

    // =========================================================
    //  TEST 1 — processPayment() with CARD → SUCCESS
    // =========================================================
    @Test
    void testProcessPayment_WithCard_ShouldReturnSuccess() {
        // ARRANGE
        doNothing().when(paymentRepository).save(any(Payment.class));

        // ACT
        PaymentResponseDto result = paymentService.processPayment(cardRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals("SUCCESS", result.getPaymentStatus());
        assertEquals(1L, result.getOrderId());
        assertEquals("CARD", result.getPaymentMethod());
        assertEquals(250.00, result.getAmount());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        // save() must be called once
    }

    // =========================================================
    //  TEST 2 — processPayment() with WALLET → SUCCESS
    // =========================================================
    @Test
    void testProcessPayment_WithWallet_ShouldReturnSuccess() {
        // ARRANGE
        doNothing().when(paymentRepository).save(any(Payment.class));

        // ACT
        PaymentResponseDto result = paymentService.processPayment(walletRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals("SUCCESS", result.getPaymentStatus());
        assertEquals(2L, result.getOrderId());
        assertEquals("WALLET", result.getPaymentMethod());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    // =========================================================
    //  TEST 3 — processPayment() with CASH → FAILED
    // =========================================================
    @Test
    void testProcessPayment_WithInvalidMethod_ShouldReturnFailed() {
        // ARRANGE
        doNothing().when(paymentRepository).save(any(Payment.class));

        // ACT
        PaymentResponseDto result = paymentService.processPayment(invalidRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals("FAILED", result.getPaymentStatus());
        // CASH is not allowed — so status must be FAILED
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    // =========================================================
    //  TEST 4 — processPayment() with lowercase "card" → SUCCESS
    // =========================================================
    @Test
    void testProcessPayment_WithLowercaseCard_ShouldReturnSuccess() {
        // ARRANGE
        doNothing().when(paymentRepository).save(any(Payment.class));
        cardRequest.setPaymentMethod("card"); // lowercase input

        // ACT
        PaymentResponseDto result = paymentService.processPayment(cardRequest);

        // ASSERT
        assertEquals("SUCCESS", result.getPaymentStatus());
        // toUpperCase() in service handles lowercase — so should still pass
    }

    // =========================================================
    //  TEST 5 — getPaymentByOrderId() → returns payment details
    // =========================================================
    @Test
    void testGetPaymentByOrderId_ShouldReturnPaymentDetails() {
        // ARRANGE
        when(paymentRepository.findByOrderId(1L))
                .thenReturn(successPayment);
        // fake repo returns successPayment when orderId=1

        // ACT
        PaymentResponseDto result = paymentService.getPaymentByOrderId(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getPaymentId());
        assertEquals(1L, result.getOrderId());
        assertEquals("CARD", result.getPaymentMethod());
        assertEquals("SUCCESS", result.getPaymentStatus());
        assertEquals("Payment fetched successfully", result.getMessage());
    }

    // =========================================================
    //  TEST 6 — isPaymentSuccessful() → true for SUCCESS
    // =========================================================
    @Test
    void testIsPaymentSuccessful_WhenSuccess_ShouldReturnTrue() {
        // ARRANGE
        when(paymentRepository.findByOrderId(1L))
                .thenReturn(successPayment);

        // ACT
        boolean result = paymentService.isPaymentSuccessful(1L);

        // ASSERT
        assertTrue(result);
        // payment status is SUCCESS so must return true
    }

    // =========================================================
    //  TEST 7 — isPaymentSuccessful() → false for FAILED
    // =========================================================
    @Test
    void testIsPaymentSuccessful_WhenFailed_ShouldReturnFalse() {
        // ARRANGE
        when(paymentRepository.findByOrderId(3L))
                .thenReturn(failedPayment);

        // ACT
        boolean result = paymentService.isPaymentSuccessful(3L);

        // ASSERT
        assertFalse(result);
        // payment status is FAILED so must return false
    }

    // =========================================================
    //  TEST 8 — processPayment() message check
    // =========================================================
    @Test
    void testProcessPayment_ShouldReturnCorrectMessage() {
        // ARRANGE
        doNothing().when(paymentRepository).save(any(Payment.class));

        // ACT
        PaymentResponseDto result = paymentService.processPayment(cardRequest);

        // ASSERT
        assertEquals("Payment processed successfully", result.getMessage());
        // message must match exactly what service returns
    }
}
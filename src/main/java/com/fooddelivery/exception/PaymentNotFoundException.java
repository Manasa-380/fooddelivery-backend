package com.fooddelivery.exception;

/**
 * Exception thrown when a payment is not found in the database.
 */
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
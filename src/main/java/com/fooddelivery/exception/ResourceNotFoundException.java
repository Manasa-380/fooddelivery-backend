package com.fooddelivery.exception;
/**
 * Thrown when a requested resource is not found in the system.
 * Example: Customer not found, User not found, Order not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    // Default constructor
    public ResourceNotFoundException() {
        super("Requested resource not found");
    }

    // Constructor with custom message
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

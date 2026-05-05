package com.fooddelivery.exception;

/**
 * Thrown when authentication fails.
 * Example: invalid email, wrong password, unauthorized access.
 */
public class AuthenticationException extends RuntimeException {

    // Default constructor
    public AuthenticationException() {
        super("Authentication failed");
    }

    // Constructor with custom message
    public AuthenticationException(String message) {
        super(message);
    }
}
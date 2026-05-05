package com.fooddelivery.exception;

/**
 * Thrown when the client sends invalid or incomplete request data.
 * Example: null fields, empty strings, invalid input values.
 */
public class InvalidRequestException extends RuntimeException {

    // Default constructor
    public InvalidRequestException() {
        super("Invalid request data");
    }

    // Constructor with custom message
    public InvalidRequestException(String message) {
        super(message);
    }
}
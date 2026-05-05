package com.fooddelivery.exception;

<<<<<<< HEAD
public class InvalidRequestException extends RuntimeException {

=======
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
>>>>>>> 07d23b6fe67222442503f39702d4273ed76dc2e9
    public InvalidRequestException(String message) {
        super(message);
    }
}
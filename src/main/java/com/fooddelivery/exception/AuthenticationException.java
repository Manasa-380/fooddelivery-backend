package com.fooddelivery.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
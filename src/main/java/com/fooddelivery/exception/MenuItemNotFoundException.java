package com.fooddelivery.exception;

/**
 * Exception thrown when a menu item is not found in the database.
 */
public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(String message) {
        super(message);
    }
}
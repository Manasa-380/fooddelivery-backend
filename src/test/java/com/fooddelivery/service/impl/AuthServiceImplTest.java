package com.fooddelivery.service.impl;

import com.fooddelivery.entity.User;
import com.fooddelivery.exception.AuthenticationException;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User(1L, "john@example.com", "Test@123", "CUSTOMER", "ACTIVE");
    }

    // =========================================================================
    //  register
    // =========================================================================

    @Test
    void register_validUser_registersSuccessfully() {
        // Email not found (new user) — throw exception to simulate not existing
        when(userRepository.findByEmail("john@example.com"))
                .thenThrow(new RuntimeException("not found"))  // first call: duplicate check
                .thenReturn(new User(1L, "john@example.com", "hashed", "CUSTOMER", "ACTIVE")); // second call: return saved

        User result = authService.register(validUser);

        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_nullUser_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class,
                () -> authService.register(null));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_nullEmail_throwsInvalidRequestException() {
        validUser.setEmail(null);
        assertThrows(InvalidRequestException.class,
                () -> authService.register(validUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_invalidEmailFormat_throwsInvalidRequestException() {
        validUser.setEmail("invalidemail");
        assertThrows(InvalidRequestException.class,
                () -> authService.register(validUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_weakPassword_throwsInvalidRequestException() {
        validUser.setPassword("password"); // no special character
        assertThrows(InvalidRequestException.class,
                () -> authService.register(validUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shortPassword_throwsInvalidRequestException() {
        validUser.setPassword("T@1"); // less than 8 characters
        assertThrows(InvalidRequestException.class,
                () -> authService.register(validUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_invalidRole_throwsInvalidRequestException() {
        validUser.setRole("ADMIN");
        assertThrows(InvalidRequestException.class,
                () -> authService.register(validUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_duplicateEmail_throwsInvalidRequestException() {
        // Simulate email already exists — findByEmail returns a user
        when(userRepository.findByEmail("john@example.com")).thenReturn(validUser);
        assertThrows(InvalidRequestException.class,
                () -> authService.register(validUser));
        verify(userRepository, never()).save(any());
    }

    // =========================================================================
    //  login
    // =========================================================================

    @Test
    void login_nullEmail_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class,
                () -> authService.login(null, "Test@123"));
    }

    @Test
    void login_nullPassword_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class,
                () -> authService.login("john@example.com", null));
    }

    @Test
    void login_emailNotFound_throwsAuthenticationException() {
        when(userRepository.findByEmail(anyString()))
                .thenThrow(new RuntimeException("not found"));
        assertThrows(AuthenticationException.class,
                () -> authService.login("unknown@example.com", "Test@123"));
    }

    @Test
    void login_wrongPassword_throwsAuthenticationException() {
        // Store a hashed password by registering first
        when(userRepository.findByEmail("john@example.com"))
                .thenThrow(new RuntimeException("not found"))
                .thenReturn(new User(1L, "john@example.com", "hashed_password", "CUSTOMER", "ACTIVE"));

        authService.register(validUser);

        // Now try login with wrong password
        User storedUser = new User(1L, "john@example.com", "wronghash", "CUSTOMER", "ACTIVE");
        when(userRepository.findByEmail("john@example.com")).thenReturn(storedUser);

        assertThrows(AuthenticationException.class,
                () -> authService.login("john@example.com", "WrongPass@1"));
    }

    @Test
    void login_inactiveAccount_throwsAuthenticationException() {
        // Register to get a real hash
        when(userRepository.findByEmail("john@example.com"))
                .thenThrow(new RuntimeException("not found"))
                .thenReturn(new User(1L, "john@example.com", "Test@123", "CUSTOMER", "ACTIVE"));

        authService.register(validUser);

        // Return inactive user on login
        User inactiveUser = new User(1L, "john@example.com", "somehash", "CUSTOMER", "INACTIVE");
        when(userRepository.findByEmail("john@example.com")).thenReturn(inactiveUser);

        assertThrows(AuthenticationException.class,
                () -> authService.login("john@example.com", "Test@123"));
    }
}
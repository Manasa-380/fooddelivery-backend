package com.fooddelivery.service.impl;

import com.fooddelivery.entity.User;
import com.fooddelivery.exception.AuthenticationException;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Implementation of {@link AuthService} handling user registration and login,
 * including BCrypt password hashing and input validation.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Set<String> ALLOWED_ROLES =
            Set.of("CUSTOMER", "RESTAURANT_OWNER", "AGENT");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[@#$%!^&*]).{8,}$");

    /**
     * Constructs an {@code AuthServiceImpl} with the given repository.
     * A new {@link BCryptPasswordEncoder} is created for hashing passwords.
     *
     * @param userRepository the repository used to access user data
     */
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Registers a new user after validating email, password, and role.
     * The password is hashed with BCrypt before being stored.
     *
     * @param user the user data to register
     * @return the saved {@link User} object retrieved from the database
     * @throws InvalidRequestException if validation fails or email is already taken
     */
    @Override
    public User register(User user) {
        if (user == null ||
                user.getEmail() == null ||
                user.getPassword() == null ||
                user.getRole() == null) {
            log.warn("Registration attempted with null user data");
            throw new InvalidRequestException("Invalid user data");
        }

        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            log.warn("Registration failed – invalid email format: {}", user.getEmail());
            throw new InvalidRequestException("Invalid email format. '@' is required");
        }

        if (!PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {
            log.warn("Registration failed – weak password for email: {}", user.getEmail());
            throw new InvalidRequestException(
                    "Password must be at least 8 characters long and contain one special character"
            );
        }

        if (!ALLOWED_ROLES.contains(user.getRole())) {
            log.warn("Registration failed – invalid role '{}' for email: {}",
                    user.getRole(), user.getEmail());
            throw new InvalidRequestException(
                    "Invalid role. Allowed roles: CUSTOMER, RESTAURANT_OWNER, AGENT"
            );
        }

        // Duplicate email check
        try {
            userRepository.findByEmail(user.getEmail());
            log.warn("Registration failed – email already registered: {}", user.getEmail());
            throw new InvalidRequestException("Email already registered");
        } catch (InvalidRequestException e) {
            throw e;  // re-throw our own exception
        } catch (Exception ignored) {
            // Expected when email does NOT exist — safe to proceed
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        log.debug("Password hashed for email: {}", user.getEmail());
        user.setPassword(hashedPassword);
        user.setStatus("ACTIVE");

        try {
            userRepository.save(user);
        } catch (DuplicateKeyException e) {
            log.error("Duplicate key on save for email: {}", user.getEmail(), e);
            throw new InvalidRequestException("Email already registered");
        }

        log.info("User registered successfully: {}", user.getEmail());
        return userRepository.findByEmail(user.getEmail());
    }

    /**
     * Authenticates a user by verifying their email and BCrypt-hashed password.
     *
     * @param email    the user's email address
     * @param password the raw (plain-text) password to verify
     * @return the authenticated {@link User}
     * @throws InvalidRequestException  if email or password is null
     * @throws AuthenticationException  if credentials are invalid or the account is inactive
     */
    @Override
    public User login(String email, String password) {
        if (email == null || password == null) {
            log.warn("Login attempted with null email or password");
            throw new InvalidRequestException("Email and password are required");
        }

        User user;
        try {
            user = userRepository.findByEmail(email);
        } catch (Exception e) {
            log.warn("Login failed – no user found for email: {}", email);
            throw new AuthenticationException("Invalid credentials");
        }

        // BCrypt comparison
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Login failed – incorrect password for email: {}", email);
            throw new AuthenticationException("Invalid credentials");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            log.warn("Login failed – inactive account for email: {}", email);
            throw new AuthenticationException("User account is inactive");
        }

        log.info("User logged in successfully: {}", email);
        return user;
    }
}
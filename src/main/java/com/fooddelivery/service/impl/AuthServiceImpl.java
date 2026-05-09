package com.service.impl;

import com.entity.User;
import com.exception.AuthenticationException;
import com.exception.InvalidRequestException;
import com.repository.UserRepository;
import com.service.AuthService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Set<String> ALLOWED_ROLES =
            Set.of("CUSTOMER", "RESTAURANT_OWNER", "AGENT");

    // ✅ Regex patterns
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[@#$%!^&*]).{8,}$");

    @Override
    public User register(User user) {

        if (user == null ||
                user.getEmail() == null ||
                user.getPassword() == null ||
                user.getRole() == null) {
            throw new InvalidRequestException("Invalid user data");
        }

        // ✅ Email validation
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new InvalidRequestException("Invalid email format. '@' is required");
        }

        // ✅ Password validation
        if (!PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {
            throw new InvalidRequestException(
                    "Password must be at least 8 characters long and contain one special character"
            );
        }

        // ✅ Role validation
        if (!ALLOWED_ROLES.contains(user.getRole())) {
            throw new InvalidRequestException(
                    "Invalid role. Allowed roles: CUSTOMER, RESTAURANT_OWNER, AGENT"
            );
        }

        // ✅ Duplicate email check
        try {
            userRepository.findByEmail(user.getEmail());
            throw new InvalidRequestException("Email already registered");
        } catch (Exception ignored) {
        }

        user.setStatus("ACTIVE");

        try {
            userRepository.save(user);
        } catch (DuplicateKeyException e) {
            throw new InvalidRequestException("Email already registered");
        }

        return userRepository.findByEmail(user.getEmail());
    }

    @Override
    public User login(String email, String password) {

        if (email == null || password == null) {
            throw new InvalidRequestException("Email and password are required");
        }

        User user;
        try {
            user = userRepository.findByEmail(email);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid credentials");
        }

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid credentials");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new AuthenticationException("User account is inactive");
        }

        return user;
    }
}
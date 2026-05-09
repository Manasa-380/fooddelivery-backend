package com.service;

import com.entity.User;

public interface AuthService {

    User register(User user);

    User login(String email, String password);
}
package com.fooddelivery.service;

import com.fooddelivery.entity.User;

public interface AuthService {

    User register(User user);

    User login(String email, String password);
}
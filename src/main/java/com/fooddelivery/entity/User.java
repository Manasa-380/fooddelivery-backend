package com.fooddelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long userId;
    private String email;
    private String password;
    private String role;   // CUSTOMER, RESTAURANT_OWNER, AGENT
    private String status; // ACTIVE / INACTIVE
}

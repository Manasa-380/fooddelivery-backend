package com.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    private String email;      // maps to users.email
    private String password;   // raw password (will be encoded)
    private String name;       // customer_name / agent_name / restaurant_name
    private String address;    // customer address / agent address
}

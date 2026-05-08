package com.fooddelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    private Long agentId;
    private Long userId;
    private String agentName;
    private String contactNumber;
    private String agentStatus; // AVAILABLE, BUSY, OFFLINE
}
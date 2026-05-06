package com.fooddelivery.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequestDto {

    private Long orderId;
    private Long agentId;
}
package com.fooddelivery.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {

    private Long deliveryId;
    private Long orderId;        // ✅ ADD THIS
    private String deliveryStatus; // ✅ better naming
    private LocalDateTime eta;
}
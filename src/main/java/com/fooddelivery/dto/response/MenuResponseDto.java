package com.fooddelivery.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MenuResponseDto {
    private Long itemId;
    private String name;
    private String description;
    private BigDecimal price;
    private Long restaurantId;
    private boolean isAvailable;
}
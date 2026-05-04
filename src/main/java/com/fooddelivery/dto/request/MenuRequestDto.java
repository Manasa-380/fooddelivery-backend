package com.fooddelivery.dto.request;

import lombok.Data;
import java.math.BigDecimal;
@Data
public class MenuRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Long restaurantId;
}
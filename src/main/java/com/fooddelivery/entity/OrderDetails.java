package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {

    private Long orderItemId;
    private Long orderId;
    private Long itemId;
    private int quantity;
    private BigDecimal price;
}
package com.service;

import com.dto.request.OrderRequestDto;
import com.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto dto);

    OrderResponseDto getOrderById(Long orderId);

    List<OrderResponseDto> getOrdersByCustomer(Long customerId);

    void updateOrderStatus(Long orderId, String status);

    String getOrderStatus(Long orderId);
}
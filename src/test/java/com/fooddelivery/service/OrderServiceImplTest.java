package com.fooddelivery.service;
import com.fooddelivery.dto.request.OrderItemRequestDto;
import com.fooddelivery.dto.request.OrderRequestDto;
import com.fooddelivery.dto.response.MenuResponseDto;
import com.fooddelivery.entity.Order;
import com.fooddelivery.entity.OrderDetails;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.OrderDetailsRepository;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.service.CustomerService;
import com.fooddelivery.service.MenuService;

import com.fooddelivery.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test cases for OrderServiceImpl.
 *
 * <p>All dependencies are mocked using Mockito.
 * These tests validate only business logic, not database behavior.</p>
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private MenuService menuService;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    /**
     * ✅ Test: Successful order placement
     */
    @Test
    void shouldPlaceOrderSuccessfully() {

        // -------- Arrange --------
        OrderItemRequestDto item = new OrderItemRequestDto(1L, 2);

        OrderRequestDto request = new OrderRequestDto();
        request.setCustomerId(1L);
        request.setRestaurantId(1L);
        request.setItems(List.of(item));

        MenuResponseDto menuResponse = new MenuResponseDto();
        menuResponse.setItemId(1L);
        menuResponse.setPrice(BigDecimal.valueOf(100));
        menuResponse.setAvailable(true);

        Order savedOrder = new Order();
        savedOrder.setOrderId(10L);
        savedOrder.setCustomerId(1L);
        savedOrder.setRestaurantId(1L);
        savedOrder.setOrderStatus("PLACED");
        savedOrder.setTotalAmount(BigDecimal.valueOf(200));

        when(menuService.getMenuItemById(1L)).thenReturn(menuResponse);
        when(orderRepository.save(any(Order.class))).thenReturn(10L);
        when(orderRepository.findById(10L)).thenReturn(savedOrder);

        // -------- Act + Assert --------
        assertDoesNotThrow(() -> orderService.placeOrder(request));

        verify(orderRepository).save(any(Order.class));
        verify(orderDetailsRepository).save(any(OrderDetails.class));
        verify(menuItemRepository).reduceQuantity(1L, 2);
        verify(orderRepository).findById(10L);
    }

    /**
     * ❌ Test: Order with no items should fail
     */
    @Test
    void shouldFailWhenOrderHasNoItems() {

        OrderRequestDto request = new OrderRequestDto();
        request.setCustomerId(1L);
        request.setRestaurantId(1L);
        request.setItems(List.of());

        InvalidRequestException ex =
                assertThrows(
                        InvalidRequestException.class,
                        () -> orderService.placeOrder(request)
                );

        assertEquals("Order must contain at least one item", ex.getMessage());
    }

    /**
     * ❌ Test: Invalid item quantity
     */
    @Test
    void shouldFailForInvalidItemQuantity() {

        OrderItemRequestDto item = new OrderItemRequestDto(1L, 0);

        OrderRequestDto request = new OrderRequestDto();
        request.setCustomerId(1L);
        request.setRestaurantId(1L);
        request.setItems(List.of(item));

        InvalidRequestException ex =
                assertThrows(
                        InvalidRequestException.class,
                        () -> orderService.placeOrder(request)
                );

        assertEquals("Item quantity must be greater than zero", ex.getMessage());
    }

    /**
     * ❌ Test: Menu item not available
     */
    @Test
    void shouldFailWhenMenuItemNotAvailable() {

        OrderItemRequestDto item = new OrderItemRequestDto(1L, 1);

        OrderRequestDto request = new OrderRequestDto();
        request.setCustomerId(1L);
        request.setRestaurantId(1L);
        request.setItems(List.of(item));

        MenuResponseDto menuResponse = new MenuResponseDto();
        menuResponse.setItemId(1L);
        menuResponse.setAvailable(false);

        when(menuService.getMenuItemById(1L)).thenReturn(menuResponse);

        InvalidRequestException ex =
                assertThrows(
                        InvalidRequestException.class,
                        () -> orderService.placeOrder(request)
                );

        assertTrue(ex.getMessage().contains("Menu item not available"));
    }

    /**
     * ❌ Test: Order not found by ID
     */
    @Test
    void shouldThrowExceptionWhenOrderNotFound() {

        when(orderRepository.findById(99L)).thenReturn(null);

        ResourceNotFoundException ex =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> orderService.getOrderById(99L)
                );

        assertTrue(ex.getMessage().contains("Order not found"));
    }

    /**
     * ✅ Test: Update order status successfully
     */
    @Test
    void shouldUpdateOrderStatusSuccessfully() {

        Order order = new Order();
        order.setOrderId(1L);
        order.setOrderStatus("PLACED");

        when(orderRepository.findById(1L)).thenReturn(order);

        assertDoesNotThrow(() ->
                orderService.updateOrderStatus(1L, "DELIVERED")
        );

        verify(orderRepository).updateStatus(1L, "DELIVERED");
    }

    /**
     * ❌ Test: Invalid order status update
     */
    @Test
    void shouldFailWhenUpdatingWithEmptyStatus() {

        InvalidRequestException ex =
                assertThrows(
                        InvalidRequestException.class,
                        () -> orderService.updateOrderStatus(1L, "")
                );

        assertEquals("Order status cannot be empty", ex.getMessage());
    }

    /**
     * ✅ Test: Get order status successfully
     */
    @Test
    void shouldReturnOrderStatusSuccessfully() {

        Order order = new Order();
        order.setOrderId(1L);
        order.setOrderStatus("DELIVERED");

        when(orderRepository.findById(1L)).thenReturn(order);

        String status = orderService.getOrderStatus(1L);

        assertEquals("DELIVERED", status);
    }
}



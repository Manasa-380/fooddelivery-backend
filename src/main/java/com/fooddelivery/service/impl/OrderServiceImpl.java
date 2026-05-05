package com.fooddelivery.service.impl;
import com.fooddelivery.dto.request.OrderItemRequestDto;
import com.fooddelivery.dto.request.OrderRequestDto;
import com.fooddelivery.dto.response.MenuResponseDto;
import com.fooddelivery.dto.response.OrderItemResponseDto;
import com.fooddelivery.dto.response.OrderResponseDto;
import com.fooddelivery.entity.Order;
import com.fooddelivery.entity.OrderDetails;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.OrderDetailsRepository;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.service.CustomerService;
import com.fooddelivery.service.MenuService;
import com.fooddelivery.service.OrderService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CustomerService customerService;
    private final MenuService menuService;
    private final MenuItemRepository menuItemRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderDetailsRepository orderDetailsRepository,
            CustomerService customerService,
            MenuService menuService,
            MenuItemRepository menuItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.customerService = customerService;
        this.menuService = menuService;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto dto) {

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new InvalidRequestException("Order must contain at least one item");
        }

        // 1. Validate customer exists (CustomerService expects int)
        customerService.getCustomerById(dto.getCustomerId().intValue());

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 2. Validate menu items & calculate total
        for (OrderItemRequestDto item : dto.getItems()) {

            if (item.getQuantity() <= 0) {
                throw new InvalidRequestException("Item quantity must be greater than zero");
            }

            MenuResponseDto menuItem =
                    menuService.getMenuItemById(item.getItemId());

            if (!menuItem.isAvailable()) {
                throw new InvalidRequestException(
                        "Menu item not available: " + item.getItemId()
                );
            }

            totalAmount = totalAmount.add(
                    menuItem.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        // 3. Save order and get generated orderId
        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setRestaurantId(dto.getRestaurantId());
        order.setOrderStatus("PLACED");
        order.setTotalAmount(totalAmount);

        Long orderId = orderRepository.save(order);

        // 4. Save order items
        for (OrderItemRequestDto item : dto.getItems()) {

            MenuResponseDto menuItem =
                    menuService.getMenuItemById(item.getItemId());

            OrderDetails details = new OrderDetails();
            details.setOrderId(orderId);
            details.setItemId(item.getItemId());
            details.setQuantity(item.getQuantity());
            details.setPrice(menuItem.getPrice());

            orderDetailsRepository.save(details);

            menuItemRepository.reduceQuantity(
                    menuItem.getItemId(),
                    item.getQuantity()
            );

        }

        return getOrderById(orderId);
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException(
                    "Order not found with id: " + orderId
            );
        }

        List<OrderDetails> items =
                orderDetailsRepository.findByOrderId(orderId);

        // Entity → DTO mapping (INLINE)
        OrderResponseDto response = new OrderResponseDto();
        response.setOrderId(order.getOrderId());
        response.setCustomerId(order.getCustomerId());
        response.setRestaurantId(order.getRestaurantId());
        response.setStatus(order.getOrderStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setOrderTime(order.getOrderTime());

        List<OrderItemResponseDto> itemDtos = items.stream()
                .map(i -> new OrderItemResponseDto(
                        i.getItemId(),
                        i.getQuantity(),
                        i.getPrice()
                ))
                .collect(Collectors.toList());

        response.setItems(itemDtos);

        return response;
    }

    @Override
    public List<OrderResponseDto> getOrdersByCustomer(Long customerId) {

        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(order -> getOrderById(order.getOrderId()))
                .toList();
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        orderRepository.updateStatus(orderId, status);
    }

    @Override
    public String getOrderStatus(Long orderId) {
        return orderRepository.findById(orderId).getOrderStatus();
    }
}

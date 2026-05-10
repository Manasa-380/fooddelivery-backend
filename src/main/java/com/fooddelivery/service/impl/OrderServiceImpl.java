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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Order Management.
 *
 * <p>This class contains the core business logic for managing customer orders
 * in the Online Food Delivery System. It supports order placement, validation,
 * total amount calculation, order retrieval, and order status updates.</p>
 *
 * <p>Logging is used to trace important operational events such as order creation,
 * validation failures, status updates, and error situations.</p>
 *
 * <p>The class is annotated with {@code @Transactional} to ensure all database
 * operations related to an order (order creation, order item insertion, and
 * inventory updates) are executed atomically. Any runtime exception will cause
 * the entire operation to roll back.</p>
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    /**
     * Logger for tracing order processing flow and errors.
     */
    private static final Logger log =
            LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CustomerService customerService;
    private final MenuService menuService;
    private final MenuItemRepository menuItemRepository;

    /**
     * Constructs an {@code OrderServiceImpl} with required dependencies.
     *
     * @param orderRepository        repository for order persistence
     * @param orderDetailsRepository repository for order item persistence
     * @param customerService        service for customer validation
     * @param menuService            service for menu item validation
     * @param menuItemRepository     repository for updating menu inventory
     */
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

    /**
     * Places a new order for a customer.
     *
     * <p>This method performs the following steps:
     * <ul>
     *   <li>Validates that the order contains at least one item</li>
     *   <li>Validates customer existence</li>
     *   <li>Validates menu item availability and quantity</li>
     *   <li>Calculates the total order amount</li>
     *   <li>Creates the order record</li>
     *   <li>Saves order item details</li>
     *   <li>Updates menu inventory</li>
     * </ul>
     * </p>
     *
     * @param dto OrderRequestDto containing customer, restaurant, and item details
     * @return OrderResponseDto containing complete order information
     * @throws InvalidRequestException if request or item details are invalid
     */
    @Override
    public OrderResponseDto placeOrder(OrderRequestDto dto) {

        log.info("Starting order placement for customerId={}", dto.getCustomerId());

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            log.warn("Order request rejected: no items provided");
            throw new InvalidRequestException("Order must contain at least one item");
        }

        // Validate customer existence
        customerService.getCustomerById(dto.getCustomerId());

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Validate menu items and calculate total amount
        for (OrderItemRequestDto item : dto.getItems()) {

            if (item.getQuantity() <= 0) {
                log.warn("Invalid quantity {} for itemId={}",
                        item.getQuantity(), item.getItemId());
                throw new InvalidRequestException("Item quantity must be greater than zero");
            }

            MenuResponseDto menuItem =
                    menuService.getMenuItemById(item.getItemId());

            if (!menuItem.isAvailable()) {
                log.warn("Menu item not available: itemId={}", item.getItemId());
                throw new InvalidRequestException(
                        "Menu item not available: " + item.getItemId()
                );
            }

            totalAmount = totalAmount.add(
                    menuItem.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        // Persist order
        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setRestaurantId(dto.getRestaurantId());
        order.setOrderStatus("PLACED");
        order.setTotalAmount(totalAmount);

        Long orderId = orderRepository.save(order);
        log.info("Order created successfully with orderId={}", orderId);

        // Persist order items and update inventory
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

        log.info("Order placement completed successfully for orderId={}", orderId);
        return getOrderById(orderId);
    }

    /**
     * Retrieves an order and its items using the order ID.
     *
     * @param orderId the unique identifier of the order
     * @return OrderResponseDto containing order and item details
     * @throws ResourceNotFoundException if the order does not exist
     */
    @Override
    public OrderResponseDto getOrderById(Long orderId) {

        log.info("Fetching order with orderId={}", orderId);

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            log.error("Order not found with orderId={}", orderId);
            throw new ResourceNotFoundException(
                    "Order not found with id: " + orderId
            );
        }

        List<OrderDetails> items =
                orderDetailsRepository.findByOrderId(orderId);

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

    /**
     * Retrieves all orders placed by a specific customer.
     *
     * @param customerId the customer ID
     * @return list of OrderResponseDto belonging to the customer
     */
    @Override
    public List<OrderResponseDto> getOrdersByCustomer(Long customerId) {

        log.info("Fetching orders for customerId={}", customerId);

        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(o -> getOrderById(o.getOrderId()))
                .toList();
    }

    /**
     * Updates the status of an existing order.
     *
     * @param orderId the order ID
     * @param status  the new status of the order
     * @throws InvalidRequestException   if status is invalid
     * @throws ResourceNotFoundException if order does not exist
     */
    @Override
    public void updateOrderStatus(Long orderId, String status) {

        log.info("Updating order status for orderId={} to status={}", orderId, status);

        if (status == null || status.isBlank()) {
            log.warn("Invalid order status provided for orderId={}", orderId);
            throw new InvalidRequestException("Order status cannot be empty");
        }

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            log.error("Order not found while updating status for orderId={}", orderId);
            throw new ResourceNotFoundException(
                    "Order not found with id: " + orderId
            );
        }

        orderRepository.updateStatus(orderId, status);
        log.info("Order status updated successfully for orderId={}", orderId);
    }

    /**
     * Retrieves the current status of an order.
     *
     * @param orderId the order ID
     * @return current order status
     * @throws ResourceNotFoundException if order does not exist
     */
    @Override
    public String getOrderStatus(Long orderId) {

        log.info("Retrieving order status for orderId={}", orderId);

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            log.error("Order not found while retrieving status for orderId={}", orderId);
            throw new ResourceNotFoundException(
                    "Order not found with id: " + orderId
            );
        }

        return order.getOrderStatus();
    }
}
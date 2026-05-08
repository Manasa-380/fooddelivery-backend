package com.fooddelivery;

import com.fooddelivery.dto.request.OrderItemRequestDto;
import com.fooddelivery.dto.request.OrderRequestDto;
import com.fooddelivery.dto.response.OrderResponseDto;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.repository.CustomerRepository;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class FooddeliveryApplication {

    private static final Logger log =
            LoggerFactory.getLogger(FooddeliveryApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FooddeliveryApplication.class, args);
    }

    @Bean
    CommandLineRunner testOrderModule(
            OrderService orderService,
            CustomerRepository customerRepository,
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository
    ) {
        return args -> {

            log.info("Starting Food Delivery Order Module");

            Scanner scanner = new Scanner(System.in);

            System.out.println("===== PLACE ORDER =====");

            // Customer
            System.out.print("Enter your name: ");
            String customerName = scanner.nextLine();
            Long customerId =
                    customerRepository.findCustomerIdByName(customerName);

            log.info("Customer selected: name={}, customerId={}", customerName, customerId);

            // Restaurant
            System.out.print("Enter restaurant name: ");
            String restaurantName = scanner.nextLine();
            Long restaurantId =
                    restaurantRepository.findRestaurantIdByName(restaurantName);

            log.info("Restaurant selected: name={}, restaurantId={}", restaurantName, restaurantId);

            // Show menu
            System.out.println("\nAvailable items:");
            List<MenuItem> menuItems =
                    menuItemRepository.findAllAvailableItemsByRestaurant(restaurantId);

            for (MenuItem m : menuItems) {
                System.out.println(
                        "- " + m.getName() +
                                " (₹" + m.getPrice() + ") – Available: " + m.getQuantity()
                );
            }

            List<OrderItemRequestDto> items = new ArrayList<>();
            Map<Long, Integer> selectedQuantities = new HashMap<>();

            // Order input loop
            while (true) {

                System.out.print("\nEnter item name from above list: ");
                String itemName = scanner.nextLine();

                MenuItem menuItem =
                        menuItemRepository.findMenuItemByName(itemName, restaurantId);

                int alreadySelected =
                        selectedQuantities.getOrDefault(menuItem.getItemId(), 0);

                int remainingStock =
                        menuItem.getQuantity() - alreadySelected;

                if (remainingStock <= 0) {
                    log.warn("Item out of stock: {}", menuItem.getName());
                    System.out.println("❌ " + menuItem.getName() + " is out of stock");
                    System.out.print("Add another item? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) break;
                    continue;
                }

                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();

                if (quantity <= 0) {
                    log.warn("Invalid quantity {} for item {}", quantity, menuItem.getName());
                    System.out.println("❌ Quantity must be greater than zero");
                    System.out.print("Add another item? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) break;
                    continue;
                }

                if (quantity > remainingStock) {
                    log.warn("Over-order attempt for item {}", menuItem.getName());
                    System.out.println(
                            "❌ Only " + remainingStock + " quantity available for " + menuItem.getName()
                    );
                    System.out.print("Add another item? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) break;
                    continue;
                }

                items.add(new OrderItemRequestDto(
                        menuItem.getItemId(),
                        quantity
                ));

                selectedQuantities.put(
                        menuItem.getItemId(),
                        alreadySelected + quantity
                );

                log.info("Item added to order: itemId={}, quantity={}",
                        menuItem.getItemId(), quantity);

                System.out.print("Add another item? (yes/no): ");
                if (!scanner.nextLine().equalsIgnoreCase("yes")) break;
            }

            // Place order
            OrderRequestDto request = new OrderRequestDto();
            request.setCustomerId(customerId);
            request.setRestaurantId(restaurantId);
            request.setItems(items);

            log.info("Placing order for customerId={}", customerId);

            OrderResponseDto response = orderService.placeOrder(request);

            log.info("Order placed successfully with orderId={}", response.getOrderId());

            System.out.println("\n✅ ORDER PLACED SUCCESSFULLY");
            System.out.println("Order ID: " + response.getOrderId());
            System.out.println("Total: " + response.getTotalAmount());
        };
    }
}
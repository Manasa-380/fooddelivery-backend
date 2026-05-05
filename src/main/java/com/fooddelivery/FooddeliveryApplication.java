//package com.fooddelivery;
//
//import com.fooddelivery.dto.request.OrderItemRequestDto;
//import com.fooddelivery.dto.request.OrderRequestDto;
//import com.fooddelivery.dto.response.OrderResponseDto;
//import com.fooddelivery.entity.MenuItem;
//import com.fooddelivery.repository.CustomerRepository;
//import com.fooddelivery.repository.MenuItemRepository;
//import com.fooddelivery.repository.RestaurantRepository;
//import com.fooddelivery.service.OrderService;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//
//import java.util.*;
//
//@SpringBootApplication
//public class FooddeliveryApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(FooddeliveryApplication.class, args);
//    }
//
//    @Bean
//    CommandLineRunner testOrderModule(
//            OrderService orderService,
//            CustomerRepository customerRepository,
//            RestaurantRepository restaurantRepository,
//            MenuItemRepository menuItemRepository
//    ) {
//        return args -> {
//
//            Scanner scanner = new Scanner(System.in);
//
//            System.out.println("===== PLACE ORDER =====");
//
//            // ✅ Customer name
//            System.out.print("Enter your name: ");
//            String customerName = scanner.nextLine();
//            Long customerId =
//                    customerRepository.findCustomerIdByName(customerName);
//
//            // ✅ Restaurant name
//            System.out.print("Enter restaurant name: ");
//            String restaurantName = scanner.nextLine();
//            Long restaurantId =
//                    restaurantRepository.findRestaurantIdByName(restaurantName);
//
//            // ✅ SHOW ONLY AVAILABLE ITEMS (quantity > 0)
//            System.out.println("\nAvailable items:");
//            List<MenuItem> menuItems =
//                    menuItemRepository.findAllAvailableItemsByRestaurant(restaurantId);
//
//            for (MenuItem m : menuItems) {
//                System.out.println(
//                        "- " + m.getName() +
//                                " (₹" + m.getPrice() + ") – Available: " + m.getQuantity()
//                );
//            }
//
//            List<OrderItemRequestDto> items = new ArrayList<>();
//            Map<Long, Integer> selectedQuantities = new HashMap<>();
//
//            // ✅ MULTI‑ITEM LOOP WITH FULL VALIDATION
//            while (true) {
//
//                System.out.print("\nEnter item name from above list: ");
//                String itemName = scanner.nextLine();
//
//                MenuItem menuItem =
//                        menuItemRepository.findMenuItemByName(itemName, restaurantId);
//
//                System.out.print("Enter quantity: ");
//                int quantity = scanner.nextInt();
//                scanner.nextLine(); // consume newline
//
//                // ✅ Block zero or negative quantity
//                if (quantity <= 0) {
//                    System.out.println("❌ Quantity must be greater than zero");
//                    continue;
//                }
//
//                int alreadySelected =
//                        selectedQuantities.getOrDefault(menuItem.getItemId(), 0);
//
//                int remainingStock =
//                        menuItem.getQuantity() - alreadySelected;
//
//                // ✅ Block out-of-stock
//                if (remainingStock <= 0) {
//                    System.out.println(
//                            "❌ " + menuItem.getName() + " is out of stock"
//                    );
//                    continue;
//                }
//
//                // ✅ Block over-ordering (cumulative)
//                if (quantity > remainingStock) {
//                    System.out.println(
//                            "❌ Only " + remainingStock +
//                                    " quantity available for " + menuItem.getName()
//                    );
//                    continue;
//                }
//
//                // ✅ Add item
//                items.add(new OrderItemRequestDto(
//                        menuItem.getItemId(),
//                        quantity
//                ));
//
//                selectedQuantities.put(
//                        menuItem.getItemId(),
//                        alreadySelected + quantity
//                );
//
//                System.out.print("Add another item? (yes/no): ");
//                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
//                    break;
//                }
//            }
//
//            // ✅ Place order
//            OrderRequestDto request = new OrderRequestDto();
//            request.setCustomerId(customerId);
//            request.setRestaurantId(restaurantId);
//            request.setItems(items);
//
//            OrderResponseDto response = orderService.placeOrder(request);
//
//            System.out.println("\n✅ ORDER PLACED SUCCESSFULLY");
//            System.out.println("Order ID: " + response.getOrderId());
//            System.out.println("Total: " + response.getTotalAmount());
//        };
//    }
//}


package com.fooddelivery;

import com.fooddelivery.dto.request.OrderItemRequestDto;
import com.fooddelivery.dto.request.OrderRequestDto;
import com.fooddelivery.dto.response.OrderResponseDto;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.repository.CustomerRepository;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.OrderService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class FooddeliveryApplication {

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

            Scanner scanner = new Scanner(System.in);

            System.out.println("===== PLACE ORDER =====");

            // ✅ Customer name
            System.out.print("Enter your name: ");
            String customerName = scanner.nextLine();
            Long customerId =
                    customerRepository.findCustomerIdByName(customerName);

            // ✅ Restaurant name
            System.out.print("Enter restaurant name: ");
            String restaurantName = scanner.nextLine();
            Long restaurantId =
                    restaurantRepository.findRestaurantIdByName(restaurantName);

            // ✅ SHOW ONLY AVAILABLE ITEMS (quantity > 0)
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

            // ✅ MULTI‑ITEM LOOP (CORRECT & SAFE)
            while (true) {

                System.out.print("\nEnter item name from above list: ");
                String itemName = scanner.nextLine();

                MenuItem menuItem =
                        menuItemRepository.findMenuItemByName(itemName, restaurantId);

                int alreadySelected =
                        selectedQuantities.getOrDefault(menuItem.getItemId(), 0);

                int remainingStock =
                        menuItem.getQuantity() - alreadySelected;

                // ✅ OUT OF STOCK
                if (remainingStock <= 0) {
                    System.out.println("❌ " + menuItem.getName() + " is out of stock");

                    System.out.print("Do you want to add another item? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        break;
                    }
                    continue;
                }

                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // consume newline

                // ✅ BLOCK ZERO / NEGATIVE
                if (quantity <= 0) {
                    System.out.println("❌ Quantity must be greater than zero");
                    continue;
                }

                // ✅ BLOCK OVER‑ORDERING (CUMULATIVE)
                if (quantity > remainingStock) {
                    System.out.println(
                            "❌ Only " + remainingStock +
                                    " quantity available for " + menuItem.getName()
                    );
                    continue;
                }

                // ✅ ADD ITEM TO ORDER
                items.add(new OrderItemRequestDto(
                        menuItem.getItemId(),
                        quantity
                ));

                selectedQuantities.put(
                        menuItem.getItemId(),
                        alreadySelected + quantity
                );

                System.out.print("Add another item? (yes/no): ");
                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                    break;
                }
            }

            // ✅ PLACE ORDER
            OrderRequestDto request = new OrderRequestDto();
            request.setCustomerId(customerId);
            request.setRestaurantId(restaurantId);
            request.setItems(items);

            OrderResponseDto response = orderService.placeOrder(request);

            System.out.println("\n✅ ORDER PLACED SUCCESSFULLY");
            System.out.println("Order ID: " + response.getOrderId());
            System.out.println("Total: " + response.getTotalAmount());
        };
    }
}
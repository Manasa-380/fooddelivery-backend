package com.fooddelivery;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;
import java.util.Scanner;
import com.fooddelivery.entity.User;
import com.fooddelivery.entity.Agent;
import com.fooddelivery.entity.Customer;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.repository.CustomerRepository;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.AuthService;
import com.fooddelivery.service.CustomerService;
import com.fooddelivery.service.DeliveryService;
import com.fooddelivery.service.MenuService;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.PaymentService;
import com.fooddelivery.service.RestaurantService;
import com.fooddelivery.dto.request.MenuRequestDto;
import com.fooddelivery.dto.request.OrderRequestDto;
import com.fooddelivery.dto.request.OrderItemRequestDto;
import com.fooddelivery.dto.request.PaymentRequestDto;
import com.fooddelivery.dto.response.MenuResponseDto;
import com.fooddelivery.dto.response.OrderResponseDto;
import com.fooddelivery.dto.response.PaymentResponseDto;
import com.fooddelivery.dto.response.DeliveryResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class FooddeliveryApplication implements CommandLineRunner {

    private final AuthService authService;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final DeliveryService deliveryService;
    private final MenuService menuService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public FooddeliveryApplication(
            AuthService authService,
            CustomerService customerService,
            RestaurantService restaurantService,
            DeliveryService deliveryService,
            MenuService menuService,
            OrderService orderService,
            PaymentService paymentService,
            CustomerRepository customerRepository,
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository) {
        this.authService = authService;
        this.customerService = customerService;
        this.restaurantService = restaurantService;
        this.deliveryService = deliveryService;
        this.menuService = menuService;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(FooddeliveryApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            log.info("===== FOOD DELIVERY SYSTEM =====");
            log.info("1. Register");
            log.info("2. Login");
            log.info("3. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.warn("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> register(scanner);
                case 2 -> login(scanner);
                case 3 -> {
                    log.info("Exiting application...");
                    running = false;
                }
                default -> log.warn("Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    // =========================================================================
    //  REGISTRATION
    // =========================================================================

    private void register(Scanner scanner) {
        try {
            log.info("===== USER REGISTRATION =====");
            User user = new User();

            log.info("Email: ");
            user.setEmail(scanner.nextLine().trim());

            log.info("Password must be at least 8 characters and contain one special character");
            System.out.print("Password: ");
            user.setPassword(scanner.nextLine().trim());

            log.info("Role (CUSTOMER / RESTAURANT_OWNER / AGENT): ");
            String role = scanner.nextLine().trim().toUpperCase();
            user.setRole(role);

            User savedUser = authService.register(user);
            log.info("User registered successfully.");

            switch (role) {
                case "CUSTOMER"         -> createCustomer(scanner, savedUser);
                case "RESTAURANT_OWNER" -> createRestaurant(scanner, savedUser);
                case "AGENT"            -> createAgent(scanner, savedUser);
                default                 -> log.warn("Unknown role - profile not created.");
            }

        } catch (InvalidRequestException e) {
            log.error("Registration failed: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
        }
    }

    private void createCustomer(Scanner scanner, User user) {
        log.info("===== CUSTOMER DETAILS =====");
        Customer customer = new Customer();
        log.info("Name: ");    customer.setCustomerName(scanner.nextLine());
        log.info("Phone: ");   customer.setPhone(scanner.nextLine());
        log.info("Address: "); customer.setAddress(scanner.nextLine());
        customer.setUserId(user.getUserId());
        customerService.createCustomer(customer);
        log.info("Customer profile created.");
    }

    private void createRestaurant(Scanner scanner, User user) {
        log.info("===== RESTAURANT DETAILS =====");
        Restaurant restaurant = new Restaurant();
        log.info("Restaurant name: "); restaurant.setRestaurantName(scanner.nextLine());
        log.info("Location: ");        restaurant.setLocation(scanner.nextLine());
        log.info("Contact number: ");  restaurant.setContactNumber(scanner.nextLine());
        restaurant.setUserId(user.getUserId());
        restaurantService.registerRestaurant(restaurant);
        log.info("Restaurant profile created.");
    }

    private void createAgent(Scanner scanner, User user) {
        log.info("===== AGENT DETAILS =====");
        Agent agent = new Agent();
        log.info("Agent name: ");     agent.setAgentName(scanner.nextLine());
        log.info("Contact number: "); agent.setContactNumber(scanner.nextLine());
        agent.setUserId(user.getUserId());
        deliveryService.createAgent(agent);
        log.info("Agent profile created.");
    }

    // =========================================================================
    //  LOGIN
    // =========================================================================

    private void login(Scanner scanner) {
        try {
            log.info("===== USER LOGIN =====");
            log.info("Email: ");
            String email = scanner.nextLine();
            log.info("Password: ");
            String password = scanner.nextLine();

            User user = authService.login(email, password);
            log.info("Logged in as {}", user.getRole());

            switch (user.getRole()) {
                case "CUSTOMER"         -> customerDashboard(scanner, user);
                case "RESTAURANT_OWNER" -> restaurantOwnerDashboard(scanner, user);
                case "AGENT"            -> agentDashboard(scanner, user);
                default                 -> log.warn("Unknown role.");
            }
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
        }
    }

    // =========================================================================
    //  CUSTOMER DASHBOARD
    // =========================================================================

    private void customerDashboard(Scanner scanner, User user) {
        Customer customer = customerService.getCustomerByUserId(user.getUserId());

        log.info("===== CUSTOMER DETAILS =====");
        log.info("Name    : {}", customer.getCustomerName());
        log.info("Phone   : {}", customer.getPhone());
        log.info("Address : {}", customer.getAddress());

        boolean running = true;
        while (running) {
            log.info("===== CUSTOMER MENU =====");
            log.info("1. View All Available Menu Items");
            log.info("2. Place an Order");
            log.info("3. Make a Payment");
            log.info("4. Check Payment Status");
            log.info("5. Logout");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.warn("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> viewAllAvailableItems();
                case 2 -> placeOrder(scanner, customer);
                case 3 -> processPayment(scanner);
                case 4 -> checkPaymentStatus(scanner);
                case 5 -> {
                    log.info("Logged out.");
                    running = false;
                }
                default -> log.warn("Invalid option.");
            }
        }
    }

    private void viewAllAvailableItems() {
        List<MenuResponseDto> items = menuService.getAllAvailableItems();
        log.info("--- All Available Items ---");
        if (items.isEmpty()) {
            log.warn("No items available right now.");
        } else {
            items.forEach(i -> log.info("{} | {} | Rs.{}",
                    i.getItemId(), i.getName(), i.getPrice()));
        }
    }

    private void placeOrder(Scanner scanner, Customer customer) {
        log.info("===== PLACE ORDER =====");

        log.info("Enter restaurant name: ");
        String restaurantName = scanner.nextLine();

        Long restaurantId = restaurantRepository.findRestaurantIdByName(restaurantName);
        if (restaurantId == null) {
            log.warn("Restaurant not found.");
            return;
        }

        List<MenuItem> menuItems =
                menuItemRepository.findAllAvailableItemsByRestaurant(restaurantId);

        if (menuItems.isEmpty()) {
            log.warn("No available items at this restaurant.");
            return;
        }

        log.info("Available items:");
        for (MenuItem m : menuItems) {
            log.info("- {} (Rs.{}) - Stock: {}", m.getName(), m.getPrice(), m.getQuantity());
        }

        List<OrderItemRequestDto> items = new ArrayList<>();
        Map<Long, Integer> selectedQuantities = new HashMap<>();

        while (true) {
            log.info("Enter item name to add: ");
            String itemName = scanner.nextLine();

            MenuItem menuItem =
                    menuItemRepository.findMenuItemByName(itemName, restaurantId);

            if (menuItem == null) {
                log.warn("Item not found in this restaurant's menu.");
            } else {
                int alreadySelected =
                        selectedQuantities.getOrDefault(menuItem.getItemId(), 0);
                int remainingStock = menuItem.getQuantity() - alreadySelected;

                if (remainingStock <= 0) {
                    log.warn("{} is out of stock.", menuItem.getName());
                } else {
                    log.info("Enter quantity: ");
                    int quantity;
                    try {
                        quantity = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        log.warn("Invalid quantity.");
                        quantity = 0;
                    }

                    if (quantity <= 0) {
                        log.warn("Quantity must be greater than zero.");
                    } else if (quantity > remainingStock) {
                        log.warn("Only {} available for {}", remainingStock, menuItem.getName());
                    } else {
                        items.add(new OrderItemRequestDto(menuItem.getItemId(), quantity));
                        selectedQuantities.put(menuItem.getItemId(), alreadySelected + quantity);
                        log.info("Added {}x {}", quantity, menuItem.getName());
                    }
                }
            }

            log.info("Add another item? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) break;
        }

        if (items.isEmpty()) {
            log.warn("No items selected. Order cancelled.");
            return;
        }

        OrderRequestDto request = new OrderRequestDto();
        request.setCustomerId(customer.getCustomerId());
        request.setRestaurantId(restaurantId);
        request.setItems(items);

        OrderResponseDto response = orderService.placeOrder(request);

        log.info("ORDER PLACED SUCCESSFULLY");
        log.info("Order ID     : {}", response.getOrderId());
        log.info("Total Amount : Rs.{}", response.getTotalAmount());
    }

    private void processPayment(Scanner scanner) {
        log.info("===== PROCESS PAYMENT =====");

        System.out.print("Enter Order ID: ");
        Long orderId;
        try {
            orderId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid Order ID.");
            return;
        }

        log.info("Payment Method (CARD / WALLET): ");
        String method = scanner.nextLine().trim();

        log.info("Enter Amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid amount.");
            return;
        }

        PaymentRequestDto request = new PaymentRequestDto();
        request.setOrderId(orderId);
        request.setPaymentMethod(method);
        request.setAmount(amount);

        PaymentResponseDto response = paymentService.processPayment(request);
        log.info("Status  : {}", response.getPaymentStatus());
        log.info("Message : {}", response.getMessage());

        if ("SUCCESS".equalsIgnoreCase(response.getPaymentStatus())) {
            log.info("Payment successful! Initiating delivery...");
            try {
                deliveryService.processDeliveryAfterPayment(orderId, "Manasa");
                log.info("Delivery has been assigned and is on the way!");
            } catch (Exception e) {
                log.warn("Delivery scheduling failed: {}", e.getMessage());
            }
        }
    }

    private void checkPaymentStatus(Scanner scanner) {
        log.info("===== PAYMENT STATUS =====");

        log.info("Enter Order ID: ");
        Long orderId;
        try {
            orderId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid Order ID.");
            return;
        }

        PaymentResponseDto fetch = paymentService.getPaymentByOrderId(orderId);
        log.info("Payment ID : {}", fetch.getPaymentId());
        log.info("Status     : {}", fetch.getPaymentStatus());

        boolean success = paymentService.isPaymentSuccessful(orderId);
        log.info("Successful : {}", success);
    }

    // =========================================================================
    //  RESTAURANT OWNER DASHBOARD
    // =========================================================================

    private void restaurantOwnerDashboard(Scanner scanner, User user) {
        Restaurant restaurant = restaurantRepository.findByUserId(user.getUserId());

        log.info("===== RESTAURANT DETAILS =====");
        log.info("Name     : {}", restaurant.getRestaurantName());
        log.info("Location : {}", restaurant.getLocation());
        log.info("Contact  : {}", restaurant.getContactNumber());

        boolean running = true;
        while (running) {
            log.info("===== RESTAURANT OWNER MENU =====");
            log.info("1. Update My Restaurant");
            log.info("2. Delete My Restaurant");
            log.info("3. View All Restaurants");
            log.info("4.  Add Menu Item");
            log.info("5.  Update Menu Item");
            log.info("6.  Delete Menu Item");
            log.info("7.  Toggle Item Availability");
            log.info("8.  View My Menu");
            log.info("9.  View Menu Item by ID");
            log.info("10. Logout");
            log.info("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.warn("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1  -> updateRestaurant(scanner, restaurant.getRestaurantId());
                case 2  -> {
                    restaurantService.deleteRestaurant(restaurant.getRestaurantId());
                    log.info("Restaurant deleted.");
                    running = false;
                }
                case 3  -> viewAllRestaurants();
                case 4  -> addMenuItem(scanner, restaurant.getRestaurantId());
                case 5  -> updateMenuItem(scanner);
                case 6  -> deleteMenuItem(scanner);
                case 7  -> toggleAvailability(scanner);
                case 8  -> viewMenuByRestaurant(restaurant.getRestaurantId());
                case 9  -> viewMenuItemById(scanner);
                case 10 -> {
                    log.info("Logged out.");
                    running = false;
                }
                default -> log.warn("Invalid option.");
            }
        }
    }

    private void updateRestaurant(Scanner scanner, Long restaurantId) {
        log.info("===== UPDATE RESTAURANT =====");
        Restaurant updated = new Restaurant();
        log.info("New Name: ");     updated.setRestaurantName(scanner.nextLine());
        log.info("New Location: "); updated.setLocation(scanner.nextLine());
        log.info("New Contact: ");  updated.setContactNumber(scanner.nextLine());
        restaurantService.updateRestaurant(restaurantId, updated);
        log.info("Restaurant updated.");
    }

    private void viewAllRestaurants() {
        List<Restaurant> list = restaurantService.getAllRestaurants();
        log.info("--- All Restaurants ---");
        list.forEach(r -> log.info("{} | {} | {} | {}",
                r.getRestaurantId(), r.getRestaurantName(),
                r.getLocation(), r.getContactNumber()));
    }

    private void addMenuItem(Scanner scanner, Long restaurantId) {
        log.info("===== ADD MENU ITEM =====");
        MenuRequestDto dto = new MenuRequestDto();
        dto.setRestaurantId(restaurantId);

        log.info("Item Name: ");    dto.setName(scanner.nextLine());
        log.info("Description: "); dto.setDescription(scanner.nextLine());
        log.info("Price: ");
        try {
            dto.setPrice(new java.math.BigDecimal(scanner.nextLine().trim()));
        } catch (NumberFormatException e) {
            log.warn("Invalid price.");
            return;
        }

        menuService.addMenuItem(dto);
        log.info("Menu item added.");
    }

    private void updateMenuItem(Scanner scanner) {
        log.info("===== UPDATE MENU ITEM =====");

        log.info("Item ID to update: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid ID.");
            return;
        }

        MenuRequestDto dto = new MenuRequestDto();
        log.info("New Name: ");        dto.setName(scanner.nextLine());
        log.info("New Description: "); dto.setDescription(scanner.nextLine());
        log.info("New Price: ");
        try {
            dto.setPrice(new java.math.BigDecimal(scanner.nextLine().trim()));
        } catch (NumberFormatException e) {
            log.warn("Invalid price.");
            return;
        }

        menuService.updateMenuItem(id, dto);
        log.info("Menu item updated.");
    }

    private void deleteMenuItem(Scanner scanner) {
        log.info("Item ID to delete: ");
        try {
            menuService.deleteMenuItem(Long.parseLong(scanner.nextLine().trim()));
            log.info("Menu item deleted.");
        } catch (NumberFormatException e) {
            log.warn("Invalid ID.");
        }
    }

    private void toggleAvailability(Scanner scanner) {
        log.info("Item ID: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid ID.");
            return;
        }

        log.info("Available? (true/false): ");
        boolean status = Boolean.parseBoolean(scanner.nextLine().trim());
        menuService.updateAvailability(id, status);
        log.info("Availability updated.");
    }

    private void viewMenuByRestaurant(Long restaurantId) {
        List<MenuResponseDto> list = menuService.getMenuByRestaurant(restaurantId);
        log.info("--- My Menu Items ---");
        if (list.isEmpty()) {
            log.warn("No items on the menu yet.");
        } else {
            list.forEach(i -> log.info("{} | {} | Rs.{}",
                    i.getItemId(), i.getName(), i.getPrice()));
        }
    }

    private void viewMenuItemById(Scanner scanner) {
        log.info("Item ID: ");
        try {
            MenuResponseDto item =
                    menuService.getMenuItemById(Long.parseLong(scanner.nextLine().trim()));
            log.info("{} | {} | Rs.{}", item.getItemId(), item.getName(), item.getPrice());
        } catch (NumberFormatException e) {
            log.warn("Invalid ID.");
        }
    }

    // =========================================================================
    //  AGENT DASHBOARD
    // =========================================================================

    private void agentDashboard(Scanner scanner, User user) {
        Agent agent = deliveryService.getAgentByUserId(user.getUserId());

        log.info("===== AGENT DETAILS =====");
        log.info("Name   : {}", agent.getAgentName());
        log.info("Phone  : {}", agent.getContactNumber());
        log.info("Status : {}", agent.getAgentStatus());

        boolean running = true;
        while (running) {
            log.info("===== AGENT MENU =====");
            log.info("1. View My Active Deliveries");
            log.info("2. Update Delivery Status");
            log.info("3. Logout");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.warn("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> viewAgentDeliveries(agent);
                case 2 -> updateDeliveryStatus(scanner);
                case 3 -> {
                    log.info("Logged out.");
                    running = false;
                }
                default -> log.warn("Invalid option.");
            }
        }
    }

    private void viewAgentDeliveries(Agent agent) {
        log.info("--- Active Deliveries ---");
        try {
            List<DeliveryResponseDto> deliveries =
                    deliveryService.getDeliveriesByAgent(agent.getAgentId());
            if (deliveries == null || deliveries.isEmpty()) {
                log.warn("No active deliveries.");
            } else {
                deliveries.forEach(d -> log.info("Delivery ID: {} | Order ID: {} | Status: {}",
                        d.getDeliveryId(), d.getOrderId(), d.getDeliveryStatus()));
            }
        } catch (Exception e) {
            log.warn("Could not fetch deliveries: {}", e.getMessage());
        }
    }

    private void updateDeliveryStatus(Scanner scanner) {
        log.info("Delivery ID: ");
        Long deliveryId;
        try {
            deliveryId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid ID.");
            return;
        }

        log.info("New Status (PICKED_UP / ON_THE_WAY / DELIVERED): ");
        String status = scanner.nextLine().trim().toUpperCase();

        try {
            deliveryService.updateDeliveryStatus(deliveryId, status);
            log.info("Delivery status updated to: {}", status);
        } catch (Exception e) {
            log.error("Update failed: {}", e.getMessage());
        }
    }
}
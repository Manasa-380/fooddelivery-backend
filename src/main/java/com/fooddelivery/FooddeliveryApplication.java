/*package com.fooddelivery;

import com.fooddelivery.entity.Agent;
import com.fooddelivery.entity.Customer;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.entity.User;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.service.AuthService;
import com.fooddelivery.service.CustomerService;
import com.fooddelivery.service.DeliveryService;
import com.fooddelivery.service.RestaurantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class FooddeliveryApplication implements CommandLineRunner {

    private final AuthService authService;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final DeliveryService deliveryService;

    public FooddeliveryApplication(AuthService authService,
                                   CustomerService customerService,
                                   RestaurantService restaurantService,
                                   DeliveryService deliveryService) {
        this.authService = authService;
        this.customerService = customerService;
        this.restaurantService = restaurantService;
        this.deliveryService = deliveryService;
    }

    public static void main(String[] args) {
        SpringApplication.run(FooddeliveryApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            log.info("\n===== FOOD DELIVERY SYSTEM =====");
            log.info("1. Register");
            log.info("2. Login");
            log.info("3. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                log.info("Invalid input");
                continue;
            }

            switch (choice) {
                case 1 -> register(scanner);
                case 2 -> login(scanner);
                case 3 -> {
                    log.info("👋 Exiting application...");
                    running = false;
                }
                default -> log.info("Invalid option");
            }
        }

        scanner.close();
    }

    // ========================= REGISTER =========================
    private void register(Scanner scanner) {

        try {
            log.info("\n===== USER REGISTRATION =====");

            User user = new User();

            System.out.print("Email: ");
            user.setEmail(scanner.nextLine().trim());

            log.info(
                    "Password must be:\n" +
                            "- At least 8 characters\n" +
                            "- Contain one special character (!@#$%^&*)"
            );
            System.out.print("Password: ");
            user.setPassword(scanner.nextLine().trim());

            System.out.print("Role (CUSTOMER / RESTAURANT_OWNER / AGENT): ");
            String role = scanner.nextLine().trim().toUpperCase();
            user.setRole(role);

            User savedUser = authService.register(user);
            log.info("User registered successfully");

            // -------- ROLE‑BASED PROFILE CREATION --------

            if ("CUSTOMER".equals(role)) {
                createCustomer(scanner, savedUser);
            }
            else if ("RESTAURANT_OWNER".equals(role)) {
                createRestaurant(scanner, savedUser);
            }
            else if ("AGENT".equals(role)) {
                createAgent(scanner, savedUser);
            } else {
                log.info("Unknown role, profile not created");
            }

        } catch (InvalidRequestException e) {
            log.info("Registration failed: " + e.getMessage());
            log.info("Please try again.");
        } catch (Exception e) {
            log.info("Unexpected error: " + e.getMessage());
        }
    }

    private void createCustomer(Scanner scanner, User user) {
        log.info("\n===== CUSTOMER DETAILS =====");

        Customer customer = new Customer();
        System.out.print("Name: ");
        customer.setCustomerName(scanner.nextLine());

        System.out.print("Phone: ");
        customer.setPhone(scanner.nextLine());

        System.out.print("Address: ");
        customer.setAddress(scanner.nextLine());

        customer.setUserId(user.getUserId());
        customerService.createCustomer(customer);

        log.info("Customer profile created");
    }

    private void createRestaurant(Scanner scanner, User user) {
        log.info("\n===== RESTAURANT DETAILS =====");

        Restaurant restaurant = new Restaurant();
        System.out.print("Restaurant name: ");
        restaurant.setRestaurantName(scanner.nextLine());

        System.out.print("Location: ");
        restaurant.setLocation(scanner.nextLine());

        System.out.print("Contact number: ");
        restaurant.setContactNumber(scanner.nextLine());

        restaurant.setUserId(user.getUserId());
        restaurantService.registerRestaurant(restaurant);

        log.info("Restaurant profile created");
    }

    private void createAgent(Scanner scanner, User user) {
        log.info("\n===== AGENT DETAILS =====");

        Agent agent = new Agent();
        System.out.print("Agent name: ");
        agent.setAgentName(scanner.nextLine());

        System.out.print("Contact number: ");
        agent.setContactNumber(scanner.nextLine());

        agent.setUserId(user.getUserId());
        deliveryService.createAgent(agent);

        log.info("Agent profile created");
    }

    // ========================= LOGIN =========================
    private void login(Scanner scanner) {

        try {
            log.info("\n===== USER LOGIN =====");

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = authService.login(email, password);
            log.info("Logged in as " + user.getRole());

            // -------- ROLE‑BASED VIEW --------

            if ("CUSTOMER".equals(user.getRole())) {
                Customer customer =
                        customerService.getCustomerByUserId(user.getUserId());

                log.info("\n===== CUSTOMER DETAILS =====");
                log.info("Name    : " + customer.getCustomerName());
                log.info("Phone   : " + customer.getPhone());
                log.info("Address : " + customer.getAddress());
            }

            else if ("RESTAURANT_OWNER".equals(user.getRole())) {
                Restaurant restaurant =
                        restaurantService.getByUserId(user.getUserId());

                log.info("\n===== RESTAURANT DETAILS =====");
                log.info("Name     : " + restaurant.getRestaurantName());
                log.info("Location : " + restaurant.getLocation());
                log.info("Contact  : " + restaurant.getContactNumber());
            }

            else if ("AGENT".equals(user.getRole())) {
                Agent agent =
                        deliveryService.getAgentByUserId(user.getUserId());

                log.info("\n===== AGENT DETAILS =====");
                log.info("Name    : " + agent.getAgentName());
                log.info("Phone   : " + agent.getContactNumber());
                log.info("Status  : " + agent.getAgentStatus());
            }

        } catch (Exception e) {
            log.info("Login failed: " + e.getMessage());
        }
    }
}*/


//--------Menu module-------


/*package com.fooddelivery;

import com.fooddelivery.dto.request.MenuRequestDto;
import com.fooddelivery.dto.response.MenuResponseDto;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.service.MenuService;
import com.fooddelivery.service.RestaurantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class FooddeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FooddeliveryApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(MenuService menuService,
								 RestaurantService restaurantService) {
		return args -> {
			Scanner sc = new Scanner(System.in);
			int choice;
			do {
				log.info("\n===== FOOD DELIVERY SYSTEM =====");
				log.info("-- Restaurant Operations --");
				log.info("1. Register Restaurant");
				log.info("2. View All Restaurants");
				log.info("3. View Restaurant By ID");
				log.info("4. Update Restaurant");
				log.info("5. Delete Restaurant");
				log.info("-- Menu Operations --");
				log.info("6. Add Menu Item");
				log.info("7. Update Menu Item");
				log.info("8. Delete Menu Item");
				log.info("9. Toggle Availability");
				log.info("10. View Menu By Restaurant");
				log.info("11. View Menu Item By ID");
				log.info("-- Customer Operations --");
				log.info("12. View All Available Items");
				log.info("-- Exit --");
				log.info("13. Exit");
				System.out.print("Enter choice: ");
				choice = sc.nextInt();
				sc.nextLine();

				switch (choice) {

					case 1 -> {
						Restaurant r = new Restaurant();
						System.out.print("User ID: ");
						r.setUserId(sc.nextLong());
						sc.nextLine();
						System.out.print("Restaurant Name: ");
						r.setRestaurantName(sc.nextLine());
						System.out.print("Location: ");
						r.setLocation(sc.nextLine());
						System.out.print("Contact Number: ");
						r.setContactNumber(sc.nextLine());
						restaurantService.registerRestaurant(r);
					}

					case 2 -> {
						List<Restaurant> list =
								restaurantService.getAllRestaurants();
						log.info("\n--- All Restaurants ---");
						list.forEach(r -> log.info(
								r.getRestaurantId() + " | " +
										r.getRestaurantName() + " | " +
										r.getLocation() + " | " +
										r.getContactNumber()));
					}

					case 3 -> {
						System.out.print("Restaurant ID: ");
						Restaurant r =
								restaurantService.getRestaurant(sc.nextLong());
						sc.nextLine();
						log.info(
								r.getRestaurantId() + " | " +
										r.getRestaurantName() + " | " +
										r.getLocation() + " | " +
										r.getContactNumber());
					}

					case 4 -> {
						System.out.print("Restaurant ID to update: ");
						Long id = sc.nextLong();
						sc.nextLine();
						Restaurant r = new Restaurant();
						System.out.print("New Name: ");
						r.setRestaurantName(sc.nextLine());
						System.out.print("New Location: ");
						r.setLocation(sc.nextLine());
						System.out.print("New Contact: ");
						r.setContactNumber(sc.nextLine());
						restaurantService.updateRestaurant(id, r);
					}

					case 5 -> {
						System.out.print("Restaurant ID to delete: ");
						restaurantService.deleteRestaurant(sc.nextLong());
						sc.nextLine();
					}

					case 6 -> {
						MenuRequestDto dto = new MenuRequestDto();
						System.out.print("Restaurant ID: ");
						dto.setRestaurantId(sc.nextLong());
						sc.nextLine();
						System.out.print("Item Name: ");
						dto.setName(sc.nextLine());
						System.out.print("Description: ");
						dto.setDescription(sc.nextLine());
						System.out.print("Price: ");
						dto.setPrice(sc.nextBigDecimal());
						sc.nextLine();
						menuService.addMenuItem(dto);
					}

					case 7 -> {
						System.out.print("Item ID to update: ");
						Long id = sc.nextLong();
						sc.nextLine();
						MenuRequestDto dto = new MenuRequestDto();
						System.out.print("New Name: ");
						dto.setName(sc.nextLine());
						System.out.print("New Description: ");
						dto.setDescription(sc.nextLine());
						System.out.print("New Price: ");
						dto.setPrice(sc.nextBigDecimal());
						sc.nextLine();
						menuService.updateMenuItem(id, dto);
					}

					case 8 -> {
						System.out.print("Item ID to delete: ");
						menuService.deleteMenuItem(sc.nextLong());
						sc.nextLine();
					}

					case 9 -> {
						System.out.print("Item ID: ");
						Long id = sc.nextLong();
						sc.nextLine();
						System.out.print("Available? (true/false): ");
						boolean status = sc.nextBoolean();
						sc.nextLine();
						menuService.updateAvailability(id, status);
					}

					case 10 -> {
						System.out.print("Restaurant ID: ");
						List<MenuResponseDto> list =
								menuService.getMenuByRestaurant(sc.nextLong());
						sc.nextLine();
						log.info("\n--- Menu Items ---");
						list.forEach(i -> log.info(
								i.getItemId() + " | " +
										i.getName() + " | Rs." +
										i.getPrice()));
					}

					case 11 -> {
						System.out.print("Item ID: ");
						MenuResponseDto item =
								menuService.getMenuItemById(sc.nextLong());
						sc.nextLine();
						log.info(
								item.getItemId() + " | " +
										item.getName() + " | Rs." +
										item.getPrice());
					}

					case 12 -> {
						List<MenuResponseDto> list =
								menuService.getAllAvailableItems();
						log.info("\n--- All Available Items ---");
						list.forEach(i -> log.info(
								i.getItemId() + " | " +
										i.getName() + " | Rs." +
										i.getPrice()));
					}

					case 13 -> log.info("Exiting... Bye!");

					default -> log.info("Invalid choice!");
				}

			} while (choice != 13);
			sc.close();
		};
	}
}*/









//----------------------order module-----------

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
//            log.info("===== PLACE ORDER =====");
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
//            log.info("\nAvailable items:");
//            List<MenuItem> menuItems =
//                    menuItemRepository.findAllAvailableItemsByRestaurant(restaurantId);
//
//            for (MenuItem m : menuItems) {
//                log.info(
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
//                    log.info("❌ Quantity must be greater than zero");
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
//                    log.info(
//                            "❌ " + menuItem.getName() + " is out of stock"
//                    );
//                    continue;
//                }
//
//                // ✅ Block over-ordering (cumulative)
//                if (quantity > remainingStock) {
//                    log.info(
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
//            log.info("\n✅ ORDER PLACED SUCCESSFULLY");
//            log.info("Order ID: " + response.getOrderId());
//            log.info("Total: " + response.getTotalAmount());
//        };
//    }
//}

//-------------------payment module---------------


/*package com.fooddelivery;

import com.fooddelivery.dto.request.PaymentRequestDto;
import com.fooddelivery.dto.response.PaymentResponseDto;
import com.fooddelivery.service.PaymentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class FooddeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FooddeliveryApplication.class, args);
	}

	// ✅ PAYMENT TEST RUNNER (TEMPORARY)
	@Bean
	public CommandLineRunner paymentRunner(PaymentService paymentService) {
		return args -> {
			Scanner scanner = new Scanner(System.in);
			boolean running = true;

			while (running) {
				log.info("\n===== PAYMENT MANAGEMENT =====");
				log.info("1. Process Payment");
				log.info("2. Get Payment by Order ID");
				log.info("3. Check If Payment Successful");
				log.info("4. Exit");
				System.out.print("Choose option: ");

				int choice = scanner.nextInt();

				switch (choice) {
					case 1 -> {
						System.out.print("Enter Order ID: ");
						Long orderId = scanner.nextLong();
						scanner.nextLine();

						System.out.print("Enter Payment Method (CARD/WALLET): ");
						String method = scanner.nextLine();

						System.out.print("Enter Amount: ");
						double amount = scanner.nextDouble();

						PaymentRequestDto request = new PaymentRequestDto();
						request.setOrderId(orderId);
						request.setPaymentMethod(method);
						request.setAmount(amount);

						PaymentResponseDto response =
								paymentService.processPayment(request);

						log.info("✅ Status  : " + response.getPaymentStatus());
						log.info("✅ Message : " + response.getMessage());
					}

					case 2 -> {
						System.out.print("Enter Order ID: ");
						Long fetchId = scanner.nextLong();

						PaymentResponseDto fetch =
								paymentService.getPaymentByOrderId(fetchId);

						log.info("Payment ID : " + fetch.getPaymentId());
						log.info("Status     : " + fetch.getPaymentStatus());
					}

					case 3 -> {
						System.out.print("Enter Order ID: ");
						Long checkId = scanner.nextLong();

						boolean success =
								paymentService.isPaymentSuccessful(checkId);

						log.info("Payment successful: " + success);
					}

					case 4 -> running = false;

					default -> log.info("❌ Invalid option");
				}
			}
			scanner.close();
		};
	}
}*/

//----------------------------delivery-mdoule-------------------
/*
package com.fooddelivery;

import com.fooddelivery.dto.request.DeliveryRequestDto;
import com.fooddelivery.dto.response.DeliveryResponseDto;
import com.fooddelivery.service.DeliveryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

    @SpringBootApplication
    public class FooddeliveryApplication {

        public static void main(String[] args) {
            SpringApplication.run(FooddeliveryApplication.class, args);
        }

        @Bean
        CommandLineRunner testAutoDelivery(DeliveryService deliveryService) {
            return args -> {
                log.info("✅ Starting delivery flow test...\n");

                deliveryService.processDeliveryAfterPayment(
                        1L,
                        "TestCustomer"
                );

                log.info("\n✅ Delivery flow test completed");
            };
        }
    }
*/





    /*@Bean
    CommandLineRunner testDeliveryModule(DeliveryService deliveryService) {

       return args -> {

            log.info("✅ Starting delivery flow test...\n");

            // ✅ QUICK TEST CALL
            deliveryService.processDeliveryAfterPayment(
                    1L,
                    "TestCustomer"
            );

            log.info("\n✅ Delivery flow test completed");




            //----------------
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {

                log.info("\n===== DELIVERY MODULE =====");
                log.info("1. Assign Delivery");
                log.info("2. Get Delivery Details by Order ID");
                log.info("3. Update Delivery Status");
                log.info("4. Exit");
                System.out.print("Choose option: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    log.info("❌ Invalid input");
                    continue;
                }

                try {
                    switch (choice) {

                        case 1 -> {
                            System.out.print("Enter Order ID: ");
                            Long orderId = Long.parseLong(scanner.nextLine());

                            System.out.print("Enter Agent ID: ");
                            Long agentId = Long.parseLong(scanner.nextLine());

                            DeliveryRequestDto dto =
                                    new DeliveryRequestDto(orderId, agentId);

                            DeliveryResponseDto response =
                                    deliveryService.assignDelivery(dto);

                            log.info("\n✅ DELIVERY ASSIGNED");
                            log.info("Delivery ID: " + response.getDeliveryId());
                            log.info("Status     : " + response.getAgentStatus());
                            log.info("ETA        : " + response.getEta());
                        }

                        case 2 -> {
                            System.out.print("Enter Order ID: ");
                            Long orderId = Long.parseLong(scanner.nextLine());

                            DeliveryResponseDto response =
                                    deliveryService.getDeliveryByOrderId(orderId);

                            log.info("\n📦 DELIVERY DETAILS");
                            log.info("Delivery ID: " + response.getDeliveryId());
                            log.info("Status     : " + response.getAgentStatus());
                            log.info("ETA        : " + response.getEta());
                        }

                        case 3 -> {
                            System.out.print("Enter Delivery ID: ");
                            Long deliveryId = Long.parseLong(scanner.nextLine());

                            System.out.print(
                                    "Enter new status (PICKED_UP / IN_TRANSIT / DELIVERED): "
                            );
                            String status = scanner.nextLine();

                            deliveryService.updateDeliveryStatus(deliveryId, status);

                            log.info("\n✅ DELIVERY STATUS UPDATED");
                        }

                        case 4 -> {
                            log.info("👋 Exiting Delivery Module...");
                            running = false;
                        }

                        default -> log.info("❌ Invalid option");
                    }

                } catch (Exception e) {
                    log.info("❌ Error: " + e.getMessage());
                }
            }
        };
    }
        }*/




package com.fooddelivery;

import com.fooddelivery.dto.request.MenuRequestDto;
import com.fooddelivery.dto.request.OrderItemRequestDto;
import com.fooddelivery.dto.request.OrderRequestDto;
import com.fooddelivery.dto.request.PaymentRequestDto;
import com.fooddelivery.dto.response.DeliveryResponseDto;
import com.fooddelivery.dto.response.MenuResponseDto;
import com.fooddelivery.dto.response.OrderResponseDto;
import com.fooddelivery.dto.response.PaymentResponseDto;
import com.fooddelivery.entity.Agent;
import com.fooddelivery.entity.Customer;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.entity.User;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class FooddeliveryApplication implements CommandLineRunner {

    // ─── All services injected via constructor ───────────────────────────────
    private static final Logger log =
            LoggerFactory.getLogger(FooddeliveryApplication.class);
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

    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SpringApplication.run(FooddeliveryApplication.class, args);
    }

    // ─── Entry point ─────────────────────────────────────────────────────────
    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== FOOD DELIVERY SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.info("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> register(scanner);
                case 2 -> login(scanner);
                case 3 -> {
                    log.info("👋 Exiting application...");
                    running = false;
                }
                default -> log.info("Invalid option. Try again.");
            }
        }

        scanner.close();
    }

    // =========================================================================
    //  REGISTRATION
    // =========================================================================

    private void register(Scanner scanner) {
        try {
            log.info("\n===== USER REGISTRATION =====");

            User user = new User();

            System.out.print("Email: ");
            user.setEmail(scanner.nextLine().trim());

            log.info(
                    "Password must be:\n- At least 8 characters\n- Contain one special character (!@#$%^&*)"
            );
            System.out.print("Password: ");
            user.setPassword(scanner.nextLine().trim());

            System.out.print("Role (CUSTOMER / RESTAURANT_OWNER / AGENT): ");
            String role = scanner.nextLine().trim().toUpperCase();
            user.setRole(role);

            User savedUser = authService.register(user);
            log.info("✅ User registered successfully.");

            switch (role) {
                case "CUSTOMER"          -> createCustomer(scanner, savedUser);
                case "RESTAURANT_OWNER"  -> createRestaurant(scanner, savedUser);
                case "AGENT"             -> createAgent(scanner, savedUser);
                default                  -> log.info("Unknown role – profile not created.");
            }

        } catch (InvalidRequestException e) {
            log.info("❌ Registration failed: {}" , e.getMessage());
            log.info("Please try again.");
        } catch (Exception e) {
            log.info("❌ Unexpected error: {}" , e.getMessage());
        }
    }

    private void createCustomer(Scanner scanner, User user) {
        log.info("\n===== CUSTOMER DETAILS =====");
        Customer customer = new Customer();

        System.out.print("Name: ");
        customer.setCustomerName(scanner.nextLine());

        System.out.print("Phone: ");
        customer.setPhone(scanner.nextLine());

        System.out.print("Address: ");
        customer.setAddress(scanner.nextLine());

        customer.setUserId(user.getUserId());
        customerService.createCustomer(customer);
        log.info("✅ Customer profile created.");
    }

    private void createRestaurant(Scanner scanner, User user) {
        log.info("\n===== RESTAURANT DETAILS =====");
        Restaurant restaurant = new Restaurant();

        System.out.print("Restaurant name: ");
        restaurant.setRestaurantName(scanner.nextLine());

        System.out.print("Location: ");
        restaurant.setLocation(scanner.nextLine());

        System.out.print("Contact number: ");
        restaurant.setContactNumber(scanner.nextLine());

        restaurant.setUserId(user.getUserId());
        restaurantService.registerRestaurant(restaurant);
        log.info("✅ Restaurant profile created.");
    }

    private void createAgent(Scanner scanner, User user) {
        log.info("\n===== AGENT DETAILS =====");
        Agent agent = new Agent();

        System.out.print("Agent name: ");
        agent.setAgentName(scanner.nextLine());

        System.out.print("Contact number: ");
        agent.setContactNumber(scanner.nextLine());

        agent.setUserId(user.getUserId());
        deliveryService.createAgent(agent);
        log.info("✅ Agent profile created.");
    }

    // =========================================================================
    //  LOGIN  →  role-based dashboard
    // =========================================================================

    private void login(Scanner scanner) {
        try {
            log.info("\n===== USER LOGIN =====");

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = authService.login(email, password);
            log.info("✅ Logged in as {}" , user.getRole());

            switch (user.getRole()) {
                case "CUSTOMER"         -> customerDashboard(scanner, user);
                case "RESTAURANT_OWNER" -> restaurantOwnerDashboard(scanner, user);
                case "AGENT"            -> agentDashboard(scanner, user);
                default                 -> log.info("Unknown role.");
            }

        } catch (Exception e) {
            log.info("❌ Login failed: {}" , e.getMessage());
        }
    }

    // =========================================================================
    //  CUSTOMER DASHBOARD
    // =========================================================================

    private void customerDashboard(Scanner scanner, User user) {
        Customer customer = customerService.getCustomerByUserId(user.getUserId());

        log.info("\n===== CUSTOMER DETAILS =====");
        log.info("Name    : {}" , customer.getCustomerName());
        log.info("Phone   : {}" , customer.getPhone());
        log.info("Address : {}" , customer.getAddress());

        boolean running = true;
        while (running) {
            log.info("\n===== CUSTOMER MENU =====");
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
                log.info("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> viewAllAvailableItems();
                case 2 -> placeOrder(scanner, customer);
                case 3 -> processPayment(scanner,customer);
                case 4 -> checkPaymentStatus(scanner);
                case 5 -> {
                    log.info("Logged out.");
                    running = false;
                }
                default -> log.info("Invalid option.");
            }
        }
    }

    // ─── View all available items (customer-facing) ──────────────────────────
    private void viewAllAvailableItems() {
        List<MenuResponseDto> items = menuService.getAllAvailableItems();
        log.info("\n--- All Available Items ---");
        if (items.isEmpty()) {
            log.info("No items available right now.");
        } else {
            items.forEach(i -> log.info(
                    "{} | {} | Rs.{}",
                    i.getItemId(),
                    i.getName(),
                    i.getPrice()
            ));
        }
    }

    // ─── Place Order ─────────────────────────────────────────────────────────
    private void placeOrder(Scanner scanner, Customer customer) {
        log.info("\n===== PLACE ORDER =====");



        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        if (restaurants.isEmpty()) {
            log.warn("No restaurants available right now.");
            return;
        }

        log.info("--- Available Restaurants ---");
        restaurants.forEach(r ->
                log.info("- {}", r.getRestaurantName())
        );



        System.out.print("Enter restaurant name: ");
        String restaurantName = scanner.nextLine();

        Long restaurantId = restaurantRepository.findRestaurantIdByName(restaurantName);
        if (restaurantId == null) {
            log.info("❌ Restaurant not found.");
            return;
        }

        List<MenuItem> menuItems =
                menuItemRepository.findAllAvailableItemsByRestaurant(restaurantId);

        if (menuItems.isEmpty()) {
            log.info("❌ No available items at this restaurant.");
            return;
        }

        log.info("\nAvailable items:");
        for (MenuItem m : menuItems) {
            log.info(
                    "- {} (₹{}) – Stock: {}",
                    m.getName(),
                    m.getPrice(),
                    m.getQuantity()
            );
        }

        List<OrderItemRequestDto> items = new ArrayList<>();
        Map<Long, Integer> selectedQuantities = new HashMap<>();

        while (true) {
            System.out.print("\nEnter item name to add: ");
            String itemName = scanner.nextLine();

            MenuItem menuItem =
                    menuItemRepository.findMenuItemByName(itemName, restaurantId);

            if (menuItem == null) {
                log.info("❌ Item not found in this restaurant's menu.");
            } else {
                int alreadySelected =
                        selectedQuantities.getOrDefault(menuItem.getItemId(), 0);
                int remainingStock = menuItem.getQuantity() - alreadySelected;

                if (remainingStock <= 0) {
                    log.info("❌ {} is out of stock." , menuItem.getName());
                } else {
                    System.out.print("Enter quantity: ");
                    int quantity;
                    try {
                        quantity = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        log.info("❌ Invalid quantity.");
                        quantity = 0;
                    }

                    if (quantity <= 0) {
                        log.info("❌ Quantity must be greater than zero.");
                    } else if (quantity > remainingStock) {
                        log.info(
                                "❌ Only {} available for {}",
                                remainingStock,
                                menuItem.getName()
                        );
                    } else {
                        items.add(new OrderItemRequestDto(menuItem.getItemId(), quantity));
                        selectedQuantities.put(
                                menuItem.getItemId(),
                                alreadySelected + quantity
                        );
                        log.info("✅ Added {} x {}", quantity, menuItem.getName());
                    }
                }
            }

            System.out.print("Add another item? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) break;
        }

        if (items.isEmpty()) {
            log.info("❌ No items selected. Order cancelled.");
            return;
        }

        OrderRequestDto request = new OrderRequestDto();
        request.setCustomerId(customer.getCustomerId());
        request.setRestaurantId(restaurantId);
        request.setItems(items);

        OrderResponseDto response = orderService.placeOrder(request);

        log.info("\n✅ ORDER PLACED SUCCESSFULLY");
        log.info("Order ID     : {}" , response.getOrderId());
        log.info("Total Amount : ₹{}" , response.getTotalAmount());
    }

    // ─── Payment ─────────────────────────────────────────────────────────────
    private void processPayment(Scanner scanner, Customer customer) {
        log.info("\n===== PROCESS PAYMENT =====");

        System.out.print("Enter Order ID: ");
        Long orderId;
        try {
            orderId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.info("❌ Invalid Order ID.");
            return;
        }

        System.out.print("Payment Method (CARD / WALLET): ");
        String method = scanner.nextLine().trim();

        System.out.print("Enter Amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.info("❌ Invalid amount.");
            return;
        }

        PaymentRequestDto request = new PaymentRequestDto();
        request.setOrderId(orderId);
        request.setPaymentMethod(method);
        request.setAmount(amount);

        PaymentResponseDto response = paymentService.processPayment(request);
        log.info("✅ Status  : {}" , response.getPaymentStatus());
        log.info("✅ Message : {}" , response.getMessage());

        // Automatically trigger delivery after successful payment
        if ("SUCCESS".equalsIgnoreCase(response.getPaymentStatus())) {
            log.info("\n🚚 Payment successful! Initiating delivery...");
            try {
                Thread deliveryThread = new Thread(() -> {
                    deliveryService.processDeliveryAfterPayment(orderId, customer.getCustomerName());
                });

                deliveryThread.start();

                try {
                    deliveryThread.join();   // ✅ WAIT until delivery finishes
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
               // log.info("✅ Delivery has been assigned and is on the way!");
            } catch (Exception e) {
                log.info("⚠️ Delivery scheduling failed: {}" , e.getMessage());
            }
        }
    }

    private void checkPaymentStatus(Scanner scanner) {
        log.info("\n===== PAYMENT STATUS =====");

        System.out.print("Enter Order ID: ");
        Long orderId;
        try {
            orderId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.info("❌ Invalid Order ID.");
            return;
        }

        PaymentResponseDto fetch = paymentService.getPaymentByOrderId(orderId);
        log.info("Payment ID : {}" , fetch.getPaymentId());
        log.info("Status     : {}" , fetch.getPaymentStatus());

        boolean success = paymentService.isPaymentSuccessful(orderId);
        log.info("Successful : {}" , success);
    }

    // =========================================================================
    //  RESTAURANT OWNER DASHBOARD
    // =========================================================================

    private void restaurantOwnerDashboard(Scanner scanner, User user) {
        Restaurant restaurant = restaurantRepository.findByUserId(user.getUserId());
        log.info("\n===== RESTAURANT DETAILS =====");
        log.info("Name     : {}" , restaurant.getRestaurantName());
        log.info("Location : {}" , restaurant.getLocation());
        log.info("Contact  : {}" , restaurant.getContactNumber());

        boolean running = true;
        while (running) {
            log.info("\n===== RESTAURANT OWNER MENU =====");
            log.info("-- Restaurant --");
            log.info("1. Update My Restaurant");
            log.info("2. Delete My Restaurant");
            log.info("3. View All Restaurants");
            log.info("-- Menu Items --");
            log.info("4.  Add Menu Item");
            log.info("5.  Update Menu Item");
            log.info("6.  Delete Menu Item");
            log.info("7.  Toggle Item Availability");
            log.info("8.  View My Menu");
            log.info("9.  View Menu Item by ID");
            log.info("10. Logout");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.info("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> updateRestaurant(scanner, restaurant.getRestaurantId());
                case 2 -> {
                    restaurantService.deleteRestaurant(restaurant.getRestaurantId());
                    log.info("✅ Restaurant deleted.");
                    running = false;   // nothing left to manage
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
                default -> log.info("Invalid option.");
            }
        }
    }

    private void updateRestaurant(Scanner scanner, Long restaurantId) {
        log.info("\n===== UPDATE RESTAURANT =====");
        Restaurant updated = new Restaurant();

        System.out.print("New Name: ");
        updated.setRestaurantName(scanner.nextLine());

        System.out.print("New Location: ");
        updated.setLocation(scanner.nextLine());

        System.out.print("New Contact: ");
        updated.setContactNumber(scanner.nextLine());

        restaurantService.updateRestaurant(restaurantId, updated);
        log.info("✅ Restaurant updated.");
    }

    private void viewAllRestaurants() {
        List<Restaurant> list = restaurantService.getAllRestaurants();
        log.info("\n--- All Restaurants ---");

        list.forEach(r -> log.info(
                "{} | {} | {} | {}",
                r.getRestaurantId(),
                r.getRestaurantName(),
                r.getLocation(),
                r.getContactNumber()
        ));
    }

    private void addMenuItem(Scanner scanner, Long restaurantId) {
        log.info("\n===== ADD MENU ITEM =====");
        MenuRequestDto dto = new MenuRequestDto();
        dto.setRestaurantId(restaurantId);

        System.out.print("Item Name: ");
        dto.setName(scanner.nextLine());

        System.out.print("Description: ");
        dto.setDescription(scanner.nextLine());

        System.out.print("Price: ");
        try {
            dto.setPrice(new java.math.BigDecimal(scanner.nextLine().trim()));
        } catch (NumberFormatException e) {
            log.info("❌ Invalid price.");
            return;
        }

        menuService.addMenuItem(dto);
        log.info("✅ Menu item added.");
    }

    private void updateMenuItem(Scanner scanner) {
        log.info("\n===== UPDATE MENU ITEM =====");

        System.out.print("Item ID to update: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.info("❌ Invalid ID.");
            return;
        }

        MenuRequestDto dto = new MenuRequestDto();

        System.out.print("New Name: ");
        dto.setName(scanner.nextLine());

        System.out.print("New Description: ");
        dto.setDescription(scanner.nextLine());

        System.out.print("New Price: ");
        try {
            dto.setPrice(new java.math.BigDecimal(scanner.nextLine().trim()));
        } catch (NumberFormatException e) {
            log.info("❌ Invalid price.");
            return;
        }

        menuService.updateMenuItem(id, dto);
        log.info("✅ Menu item updated.");
    }

    private void deleteMenuItem(Scanner scanner) {
        System.out.print("Item ID to delete: ");
        try {
            menuService.deleteMenuItem(Long.parseLong(scanner.nextLine().trim()));
            log.info("✅ Menu item deleted.");
        } catch (NumberFormatException e) {
            log.info("❌ Invalid ID.");
        }
    }

    private void toggleAvailability(Scanner scanner) {
        System.out.print("Item ID: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.info("❌ Invalid ID.");
            return;
        }

        System.out.print("Available? (true/false): ");
        boolean status = Boolean.parseBoolean(scanner.nextLine().trim());
        menuService.updateAvailability(id, status);
        log.info("✅ Availability updated.");
    }

    private void viewMenuByRestaurant(Long restaurantId) {
        List<MenuResponseDto> list = menuService.getMenuByRestaurant(restaurantId);
        log.info("\n--- My Menu Items ---");
        if (list.isEmpty()) {
            log.info("No items on the menu yet.");
        } else {
            list.forEach(i -> log.info(
                    "{} | {} | Rs.{}",
                    i.getItemId(),
                    i.getName(),
                    i.getPrice()
            ));

        }
    }

    private void viewMenuItemById(Scanner scanner) {
        System.out.print("Item ID: ");
        try {
            MenuResponseDto item =
                    menuService.getMenuItemById(Long.parseLong(scanner.nextLine().trim()));
            log.info("{} | {} | Rs.{}",
                    item.getItemId(),
                    item.getName(),
                    item.getPrice()
            );
        } catch (NumberFormatException e) {
            log.info("❌ Invalid ID.");
        }
    }

    // =========================================================================
    //  AGENT DASHBOARD
    // =========================================================================

    private void agentDashboard(Scanner scanner, User user) {
        Agent agent = deliveryService.getAgentByUserId(user.getUserId());

        log.info("\n===== AGENT DETAILS =====");
        log.info("Name   : {}" , agent.getAgentName());
        log.info("Phone  : {}" , agent.getContactNumber());
        log.info("Status : {}" , agent.getAgentStatus());

        boolean running = true;
        while (running) {
            log.info("\n===== AGENT MENU =====");
            log.info("1. View My Active Deliveries");
            log.info("2. Update Delivery Status");
            log.info("3. Logout");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.info("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> viewAgentDeliveries(agent);
                case 2 -> updateDeliveryStatus(scanner);
                case 3 -> {
                    log.info("Logged out.");
                    running = false;
                }
                default -> log.info("Invalid option.");
            }
        }
    }

    private void viewAgentDeliveries(Agent agent) {
        log.info("\n--- Active Deliveries ---");
        try {
            List<DeliveryResponseDto> deliveries =
                    deliveryService.getDeliveriesByAgent(agent.getAgentId());
            if (deliveries == null || deliveries.isEmpty()) {
                log.info("No active deliveries.");
            } else {
                deliveries.forEach(d -> log.info(
                        "Delivery ID: {} | Order ID: {} | Status: {}",
                        d.getDeliveryId(),
                        d.getOrderId(),
                        d.getDeliveryStatus()
                ));
            }
        } catch (Exception e) {
            log.info("⚠️ Could not fetch deliveries: {}" , e.getMessage());
        }
    }

    private void updateDeliveryStatus(Scanner scanner) {
        System.out.print("Delivery ID: ");
        Long deliveryId;
        try {
            deliveryId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            log.info("❌ Invalid ID.");
            return;
        }

        System.out.print("New Status (PICKED_UP / ON_THE_WAY / DELIVERED): ");
        String status = scanner.nextLine().trim().toUpperCase();

        try {
            deliveryService.updateDeliveryStatus(deliveryId, status);
            log.info("✅ Delivery status updated to: {}" , status);
        } catch (Exception e) {
            log.info("❌ Update failed: {}" , e.getMessage());
        }
    }
}


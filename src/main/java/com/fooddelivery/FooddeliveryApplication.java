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
            System.out.println("\n===== FOOD DELIVERY SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                continue;
            }

            switch (choice) {
                case 1 -> register(scanner);
                case 2 -> login(scanner);
                case 3 -> {
                    System.out.println("👋 Exiting application...");
                    running = false;
                }
                default -> System.out.println("Invalid option");
            }
        }

        scanner.close();
    }

    // ========================= REGISTER =========================
    private void register(Scanner scanner) {

        try {
            System.out.println("\n===== USER REGISTRATION =====");

            User user = new User();

            System.out.print("Email: ");
            user.setEmail(scanner.nextLine().trim());

            System.out.println(
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
            System.out.println("User registered successfully");

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
                System.out.println("Unknown role, profile not created");
            }

        } catch (InvalidRequestException e) {
            System.out.println("Registration failed: " + e.getMessage());
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void createCustomer(Scanner scanner, User user) {
        System.out.println("\n===== CUSTOMER DETAILS =====");

        Customer customer = new Customer();
        System.out.print("Name: ");
        customer.setCustomerName(scanner.nextLine());

        System.out.print("Phone: ");
        customer.setPhone(scanner.nextLine());

        System.out.print("Address: ");
        customer.setAddress(scanner.nextLine());

        customer.setUserId(user.getUserId());
        customerService.createCustomer(customer);

        System.out.println("Customer profile created");
    }

    private void createRestaurant(Scanner scanner, User user) {
        System.out.println("\n===== RESTAURANT DETAILS =====");

        Restaurant restaurant = new Restaurant();
        System.out.print("Restaurant name: ");
        restaurant.setRestaurantName(scanner.nextLine());

        System.out.print("Location: ");
        restaurant.setLocation(scanner.nextLine());

        System.out.print("Contact number: ");
        restaurant.setContactNumber(scanner.nextLine());

        restaurant.setUserId(user.getUserId());
        restaurantService.registerRestaurant(restaurant);

        System.out.println("Restaurant profile created");
    }

    private void createAgent(Scanner scanner, User user) {
        System.out.println("\n===== AGENT DETAILS =====");

        Agent agent = new Agent();
        System.out.print("Agent name: ");
        agent.setAgentName(scanner.nextLine());

        System.out.print("Contact number: ");
        agent.setContactNumber(scanner.nextLine());

        agent.setUserId(user.getUserId());
        deliveryService.createAgent(agent);

        System.out.println("Agent profile created");
    }

    // ========================= LOGIN =========================
    private void login(Scanner scanner) {

        try {
            System.out.println("\n===== USER LOGIN =====");

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = authService.login(email, password);
            System.out.println("Logged in as " + user.getRole());

            // -------- ROLE‑BASED VIEW --------

            if ("CUSTOMER".equals(user.getRole())) {
                Customer customer =
                        customerService.getCustomerByUserId(user.getUserId());

                System.out.println("\n===== CUSTOMER DETAILS =====");
                System.out.println("Name    : " + customer.getCustomerName());
                System.out.println("Phone   : " + customer.getPhone());
                System.out.println("Address : " + customer.getAddress());
            }

            else if ("RESTAURANT_OWNER".equals(user.getRole())) {
                Restaurant restaurant =
                        restaurantService.getByUserId(user.getUserId());

                System.out.println("\n===== RESTAURANT DETAILS =====");
                System.out.println("Name     : " + restaurant.getRestaurantName());
                System.out.println("Location : " + restaurant.getLocation());
                System.out.println("Contact  : " + restaurant.getContactNumber());
            }

            else if ("AGENT".equals(user.getRole())) {
                Agent agent =
                        deliveryService.getAgentByUserId(user.getUserId());

                System.out.println("\n===== AGENT DETAILS =====");
                System.out.println("Name    : " + agent.getAgentName());
                System.out.println("Phone   : " + agent.getContactNumber());
                System.out.println("Status  : " + agent.getAgentStatus());
            }

        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
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
				System.out.println("\n===== FOOD DELIVERY SYSTEM =====");
				System.out.println("-- Restaurant Operations --");
				System.out.println("1. Register Restaurant");
				System.out.println("2. View All Restaurants");
				System.out.println("3. View Restaurant By ID");
				System.out.println("4. Update Restaurant");
				System.out.println("5. Delete Restaurant");
				System.out.println("-- Menu Operations --");
				System.out.println("6. Add Menu Item");
				System.out.println("7. Update Menu Item");
				System.out.println("8. Delete Menu Item");
				System.out.println("9. Toggle Availability");
				System.out.println("10. View Menu By Restaurant");
				System.out.println("11. View Menu Item By ID");
				System.out.println("-- Customer Operations --");
				System.out.println("12. View All Available Items");
				System.out.println("-- Exit --");
				System.out.println("13. Exit");
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
						System.out.println("\n--- All Restaurants ---");
						list.forEach(r -> System.out.println(
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
						System.out.println(
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
						System.out.println("\n--- Menu Items ---");
						list.forEach(i -> System.out.println(
								i.getItemId() + " | " +
										i.getName() + " | Rs." +
										i.getPrice()));
					}

					case 11 -> {
						System.out.print("Item ID: ");
						MenuResponseDto item =
								menuService.getMenuItemById(sc.nextLong());
						sc.nextLine();
						System.out.println(
								item.getItemId() + " | " +
										item.getName() + " | Rs." +
										item.getPrice());
					}

					case 12 -> {
						List<MenuResponseDto> list =
								menuService.getAllAvailableItems();
						System.out.println("\n--- All Available Items ---");
						list.forEach(i -> System.out.println(
								i.getItemId() + " | " +
										i.getName() + " | Rs." +
										i.getPrice()));
					}

					case 13 -> System.out.println("Exiting... Bye!");

					default -> System.out.println("Invalid choice!");
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
				System.out.println("\n===== PAYMENT MANAGEMENT =====");
				System.out.println("1. Process Payment");
				System.out.println("2. Get Payment by Order ID");
				System.out.println("3. Check If Payment Successful");
				System.out.println("4. Exit");
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

						System.out.println("✅ Status  : " + response.getPaymentStatus());
						System.out.println("✅ Message : " + response.getMessage());
					}

					case 2 -> {
						System.out.print("Enter Order ID: ");
						Long fetchId = scanner.nextLong();

						PaymentResponseDto fetch =
								paymentService.getPaymentByOrderId(fetchId);

						System.out.println("Payment ID : " + fetch.getPaymentId());
						System.out.println("Status     : " + fetch.getPaymentStatus());
					}

					case 3 -> {
						System.out.print("Enter Order ID: ");
						Long checkId = scanner.nextLong();

						boolean success =
								paymentService.isPaymentSuccessful(checkId);

						System.out.println("Payment successful: " + success);
					}

					case 4 -> running = false;

					default -> System.out.println("❌ Invalid option");
				}
			}
			scanner.close();
		};
	}
}*/


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
                System.out.println("✅ Starting delivery flow test...\n");

                deliveryService.processDeliveryAfterPayment(
                        1L,
                        "TestCustomer"
                );

                System.out.println("\n✅ Delivery flow test completed");
            };
        }
    }






    /*@Bean
    CommandLineRunner testDeliveryModule(DeliveryService deliveryService) {

       return args -> {

            System.out.println("✅ Starting delivery flow test...\n");

            // ✅ QUICK TEST CALL
            deliveryService.processDeliveryAfterPayment(
                    1L,
                    "TestCustomer"
            );

            System.out.println("\n✅ Delivery flow test completed");




            //----------------
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {

                System.out.println("\n===== DELIVERY MODULE =====");
                System.out.println("1. Assign Delivery");
                System.out.println("2. Get Delivery Details by Order ID");
                System.out.println("3. Update Delivery Status");
                System.out.println("4. Exit");
                System.out.print("Choose option: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input");
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

                            System.out.println("\n✅ DELIVERY ASSIGNED");
                            System.out.println("Delivery ID: " + response.getDeliveryId());
                            System.out.println("Status     : " + response.getAgentStatus());
                            System.out.println("ETA        : " + response.getEta());
                        }

                        case 2 -> {
                            System.out.print("Enter Order ID: ");
                            Long orderId = Long.parseLong(scanner.nextLine());

                            DeliveryResponseDto response =
                                    deliveryService.getDeliveryByOrderId(orderId);

                            System.out.println("\n📦 DELIVERY DETAILS");
                            System.out.println("Delivery ID: " + response.getDeliveryId());
                            System.out.println("Status     : " + response.getAgentStatus());
                            System.out.println("ETA        : " + response.getEta());
                        }

                        case 3 -> {
                            System.out.print("Enter Delivery ID: ");
                            Long deliveryId = Long.parseLong(scanner.nextLine());

                            System.out.print(
                                    "Enter new status (PICKED_UP / IN_TRANSIT / DELIVERED): "
                            );
                            String status = scanner.nextLine();

                            deliveryService.updateDeliveryStatus(deliveryId, status);

                            System.out.println("\n✅ DELIVERY STATUS UPDATED");
                        }

                        case 4 -> {
                            System.out.println("👋 Exiting Delivery Module...");
                            running = false;
                        }

                        default -> System.out.println("❌ Invalid option");
                    }

                } catch (Exception e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
            }
        };
    }
        }*/







package com.fooddelivery;

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
			System.out.println("\n===== FOOD DELIVERY MENU =====");
			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			System.out.print("Enter choice: ");

			int choice = Integer.parseInt(scanner.nextLine());

			switch (choice) {
				case 1 -> register(scanner);
				case 2 -> login(scanner);
				case 3 -> {
					System.out.println("👋 Exiting application...");
					running = false;
				}
				default -> System.out.println("❌ Invalid option");
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

			System.out.print("Password: ");
			user.setPassword(scanner.nextLine().trim());

			System.out.print("Role (CUSTOMER / RESTAURANT_OWNER / AGENT): ");
			String role = scanner.nextLine().trim().toUpperCase();
			user.setRole(role);

			// ✅ Auth-level validation happens here
			User savedUser = authService.register(user);
			System.out.println("✅ User registered successfully");

			// -------- ROLE‑BASED PROFILE CREATION --------

			if ("CUSTOMER".equals(role)) {
				System.out.println("\n===== CUSTOMER DETAILS =====");

				Customer customer = new Customer();

				System.out.print("Name: ");
				customer.setCustomerName(scanner.nextLine());

				System.out.print("Phone (10 digits): ");
				String phone = scanner.nextLine().trim();
				if (!phone.matches("\\d{10}")) {
					throw new IllegalArgumentException("Phone number must be exactly 10 digits");
				}
				customer.setPhone(phone);

				System.out.print("Address: ");
				customer.setAddress(scanner.nextLine());

				customer.setUserId(savedUser.getUserId());
				customerService.createCustomer(customer);

				System.out.println("✅ Customer profile created");
			}

			else if ("RESTAURANT_OWNER".equals(role)) {
				System.out.println("\n===== RESTAURANT DETAILS =====");

				Restaurant restaurant = new Restaurant();

				System.out.print("Restaurant name: ");
				restaurant.setRestaurantName(scanner.nextLine());

				System.out.print("Location: ");
				restaurant.setLocation(scanner.nextLine());

				System.out.print("Contact number (10 digits): ");
				String contact = scanner.nextLine().trim();
				if (!contact.matches("\\d{10}")) {
					throw new IllegalArgumentException("Contact number must be exactly 10 digits");
				}
				restaurant.setContactNumber(contact);

				restaurant.setUserId(savedUser.getUserId());
				restaurantService.registerRestaurant(restaurant);

				System.out.println("✅ Restaurant profile created");
			}

			else if ("AGENT".equals(role)) {
				System.out.println("\n===== AGENT DETAILS =====");

				Agent agent = new Agent();

				System.out.print("Agent name: ");
				agent.setAgentName(scanner.nextLine());

				System.out.print("Contact number (10 digits): ");
				String contact = scanner.nextLine().trim();
				if (!contact.matches("\\d{10}")) {
					throw new IllegalArgumentException("Contact number must be exactly 10 digits");
				}
				agent.setContactNumber(contact);

				agent.setUserId(savedUser.getUserId());
				deliveryService.createAgent(agent);

				System.out.println("✅ Agent profile created");
			}

		} catch (InvalidRequestException | IllegalArgumentException e) {
			System.out.println("❌ " + e.getMessage());
			System.out.println("Please try again.\n");
		}
	}


	// ========================= LOGIN =========================
	private void login(Scanner scanner) {

		System.out.println("\n===== USER LOGIN =====");

		System.out.print("Email: ");
		String email = scanner.nextLine();

		System.out.print("Password: ");
		String password = scanner.nextLine();

		User user = authService.login(email, password);
		System.out.println("✅ Logged in as " + user.getRole());

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
	}
}
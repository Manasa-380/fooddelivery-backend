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
}
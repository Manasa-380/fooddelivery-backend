package com.fooddelivery;

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
}


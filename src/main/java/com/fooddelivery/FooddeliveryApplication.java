package com.fooddelivery;

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
}
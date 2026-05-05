package com.fooddelivery;

import com.fooddelivery.dto.response.DeliveryResponseDto;
import com.fooddelivery.service.DeliveryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class FooddeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(FooddeliveryApplication.class, args);
    }

    // ✅ PLACE IT HERE (inside class, outside main)
    @Bean
    CommandLineRunner testDeliveryOutput(JdbcTemplate jdbcTemplate,
                                         DeliveryService deliveryService) {
        return args -> {

            // ✅ PRINT DATABASE NAME USED BY SPRING
            String dbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
            System.out.println("✅ Spring is connected to DB: " + dbName);

            System.out.println("===== DELIVERY MODULE OUTPUT TEST =====");

            Long orderId = 3L; // change this if needed

            try {
                deliveryService.assignDelivery(orderId);
                System.out.println("✅ Agent assigned successfully for Order ID: " + orderId);

                DeliveryResponseDto response =
                        deliveryService.getDeliveryByOrderId(orderId);

                System.out.println("Delivery ID : " + response.getDeliveryId());
                System.out.println("Status      : " + response.getAgentStatus());
                System.out.println("ETA         : " + response.getEta());

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("===== TEST COMPLETED =====");
        };
    }
}

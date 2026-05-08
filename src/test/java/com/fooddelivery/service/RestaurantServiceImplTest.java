package com.fooddelivery.service;

import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    // fake repository — no real DB needed

    @InjectMocks
    private RestaurantServiceImpl restaurantService;
    // real service with fake repository injected

    private Restaurant sampleRestaurant;
    private Restaurant updatedRestaurant;

    @BeforeEach
    void setUp() {
        // sample restaurant — used across all tests
        sampleRestaurant = new Restaurant();
        sampleRestaurant.setRestaurantId(1L);
        sampleRestaurant.setUserId(10L);
        sampleRestaurant.setRestaurantName("A2B");
        sampleRestaurant.setLocation("Chennai");
        sampleRestaurant.setContactNumber("9000000011");

        // updated restaurant details
        updatedRestaurant = new Restaurant();
        updatedRestaurant.setRestaurantName("A2B Updated");
        updatedRestaurant.setLocation("Bangalore");
        updatedRestaurant.setContactNumber("9000000099");
    }

    // =========================================================
    //  TEST 1 — registerRestaurant()
    // =========================================================
    @Test
    void testRegisterRestaurant_ShouldSaveSuccessfully() {
        // ARRANGE
        doNothing().when(restaurantRepository).save(any(Restaurant.class));

        // ACT
        restaurantService.registerRestaurant(sampleRestaurant);

        // ASSERT
        verify(restaurantRepository, times(1)).save(sampleRestaurant);
        // save() must be called exactly once with our restaurant
    }

    // =========================================================
    //  TEST 2 — getRestaurant() → returns correct restaurant
    // =========================================================
    @Test
    void testGetRestaurant_ShouldReturnRestaurant() {
        // ARRANGE
        when(restaurantRepository.findById(1L))
                .thenReturn(sampleRestaurant);

        // ACT
        Restaurant result = restaurantService.getRestaurant(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getRestaurantId());
        assertEquals("A2B", result.getRestaurantName());
        assertEquals("Chennai", result.getLocation());
        assertEquals("9000000011", result.getContactNumber());
    }

    // =========================================================
    //  TEST 3 — getAllRestaurants() → returns list
    // =========================================================
    @Test
    void testGetAllRestaurants_ShouldReturnList() {
        // ARRANGE
        when(restaurantRepository.findAll())
                .thenReturn(Arrays.asList(sampleRestaurant));

        // ACT
        List<Restaurant> result = restaurantService.getAllRestaurants();

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("A2B", result.get(0).getRestaurantName());
        assertEquals("Chennai", result.get(0).getLocation());
    }

    // =========================================================
    //  TEST 4 — getAllRestaurants() → empty list
    // =========================================================
    @Test
    void testGetAllRestaurants_WhenEmpty_ShouldReturnEmptyList() {
        // ARRANGE
        when(restaurantRepository.findAll())
                .thenReturn(Collections.emptyList());

        // ACT
        List<Restaurant> result = restaurantService.getAllRestaurants();

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
        // no restaurants in DB — list should be empty
    }

    // =========================================================
    //  TEST 5 — updateRestaurant() → sets ID and updates
    // =========================================================
    @Test
    void testUpdateRestaurant_ShouldSetIdAndUpdate() {
        // ARRANGE
        doNothing().when(restaurantRepository).update(any(Restaurant.class));

        // ACT
        restaurantService.updateRestaurant(1L, updatedRestaurant);

        // ASSERT
        assertEquals(1L, updatedRestaurant.getRestaurantId());
        // service must set restaurantId before calling update
        verify(restaurantRepository, times(1)).update(updatedRestaurant);
    }

    // =========================================================
    //  TEST 6 — updateRestaurant() → correct new values
    // =========================================================
    @Test
    void testUpdateRestaurant_ShouldHaveUpdatedValues() {
        // ARRANGE
        doNothing().when(restaurantRepository).update(any(Restaurant.class));

        // ACT
        restaurantService.updateRestaurant(1L, updatedRestaurant);

        // ASSERT
        assertEquals("A2B Updated", updatedRestaurant.getRestaurantName());
        assertEquals("Bangalore", updatedRestaurant.getLocation());
        assertEquals("9000000099", updatedRestaurant.getContactNumber());
    }

    // =========================================================
    //  TEST 7 — deleteRestaurant()
    // =========================================================
    @Test
    void testDeleteRestaurant_ShouldDeleteSuccessfully() {
        // ARRANGE
        doNothing().when(restaurantRepository).delete(anyLong());

        // ACT
        restaurantService.deleteRestaurant(1L);

        // ASSERT
        verify(restaurantRepository, times(1)).delete(1L);
        // delete() must be called exactly once with correct ID
    }

    // =========================================================
    //  TEST 8 — getRestaurant() → multiple restaurants
    // =========================================================
    @Test
    void testGetAllRestaurants_ShouldReturnMultipleRestaurants() {
        // ARRANGE
        Restaurant secondRestaurant = new Restaurant();
        secondRestaurant.setRestaurantId(2L);
        secondRestaurant.setRestaurantName("DALCHINE");
        secondRestaurant.setLocation("Bangalore");
        secondRestaurant.setContactNumber("9000000022");

        when(restaurantRepository.findAll())
                .thenReturn(Arrays.asList(sampleRestaurant, secondRestaurant));

        // ACT
        List<Restaurant> result = restaurantService.getAllRestaurants();

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
        // should return both restaurants
        assertEquals("A2B", result.get(0).getRestaurantName());
        assertEquals("DALCHINE", result.get(1).getRestaurantName());
    }
}
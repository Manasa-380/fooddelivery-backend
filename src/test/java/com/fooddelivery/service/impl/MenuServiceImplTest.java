package com.fooddelivery.service.impl;
import com.fooddelivery.dto.request.MenuRequestDto;
import com.fooddelivery.dto.response.MenuResponseDto;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.service.impl.MenuServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceImplTest {

    @Mock
    private MenuItemRepository menuItemRepository;
    // fake repository — no real DB hit

    @InjectMocks
    private MenuServiceImpl menuService;
    // real service with fake repository injected

    private MenuItem sampleItem;
    private MenuRequestDto sampleDto;

    @BeforeEach
    void setUp() {
        // sample MenuItem — returned by fake repository
        sampleItem = new MenuItem();
        sampleItem.setItemId(1L);
        sampleItem.setName("Chicken Biryani");
        sampleItem.setDescription("Spicy biryani");
        sampleItem.setPrice(new BigDecimal("250.00"));
        sampleItem.setRestaurantId(1L);
        sampleItem.setAvailable(true);
        sampleItem.setQuantity(20);

        // sample request DTO — what user sends as input
        sampleDto = new MenuRequestDto();
        sampleDto.setName("Chicken Biryani");
        sampleDto.setDescription("Spicy biryani");
        sampleDto.setPrice(new BigDecimal("250.00"));
        sampleDto.setRestaurantId(1L);
    }

    // =========================================================
    //  TEST 1 — addMenuItem()
    // =========================================================
    @Test
    void testAddMenuItem_ShouldSaveSuccessfully() {
        // ARRANGE
        doNothing().when(menuItemRepository).save(any(MenuItem.class));
        // doNothing = save() returns void, so we just say "do nothing when called"

        // ACT
        menuService.addMenuItem(sampleDto);

        // ASSERT
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
        // verify save() was called exactly once
    }

    // =========================================================
    //  TEST 2 — updateMenuItem()
    // =========================================================
    @Test
    void testUpdateMenuItem_ShouldUpdateSuccessfully() {
        // ARRANGE
        doNothing().when(menuItemRepository).update(any(MenuItem.class));

        // ACT
        menuService.updateMenuItem(1L, sampleDto);

        // ASSERT
        verify(menuItemRepository, times(1)).update(any(MenuItem.class));
    }

    // =========================================================
    //  TEST 3 — deleteMenuItem()
    // =========================================================
    @Test
    void testDeleteMenuItem_ShouldDeleteSuccessfully() {
        // ARRANGE
        doNothing().when(menuItemRepository).delete(anyLong());

        // ACT
        menuService.deleteMenuItem(1L);

        // ASSERT
        verify(menuItemRepository, times(1)).delete(1L);
    }

    // =========================================================
    //  TEST 4 — getMenuByRestaurant()
    // =========================================================
    @Test
    void testGetMenuByRestaurant_ShouldReturnList() {
        // ARRANGE
        when(menuItemRepository.findByRestaurantId(1L))
                .thenReturn(Arrays.asList(sampleItem));
        // fake repo returns list with 1 item

        // ACT
        List<MenuResponseDto> result = menuService.getMenuByRestaurant(1L);

        // ASSERT
        assertNotNull(result);                               // list should not be null
        assertEquals(1, result.size());                      // should have 1 item
        assertEquals("Chicken Biryani", result.get(0).getName()); // name should match
        assertEquals(new BigDecimal("250.00"), result.get(0).getPrice()); // price match
    }

    // =========================================================
    //  TEST 5 — getMenuByRestaurant() empty case
    // =========================================================
    @Test
    void testGetMenuByRestaurant_WhenEmpty_ShouldReturnEmptyList() {
        // ARRANGE
        when(menuItemRepository.findByRestaurantId(1L))
                .thenReturn(Collections.emptyList());
        // fake repo returns empty list

        // ACT
        List<MenuResponseDto> result = menuService.getMenuByRestaurant(1L);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty()); // list should be empty
    }

    // =========================================================
    //  TEST 6 — getMenuItemById()
    // =========================================================
    @Test
    void testGetMenuItemById_ShouldReturnItem() {
        // ARRANGE
        when(menuItemRepository.findById(1L))
                .thenReturn(sampleItem);

        // ACT
        MenuResponseDto result = menuService.getMenuItemById(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getItemId());
        assertEquals("Chicken Biryani", result.getName());
        assertEquals(new BigDecimal("250.00"), result.getPrice());
    }

    // =========================================================
    //  TEST 7 — getAllAvailableItems()
    // =========================================================
    @Test
    void testGetAllAvailableItems_ShouldReturnAvailableItems() {
        // ARRANGE
        when(menuItemRepository.findAllAvailable())
                .thenReturn(Arrays.asList(sampleItem));

        // ACT
        List<MenuResponseDto> result = menuService.getAllAvailableItems();

        // ASSERT
        assertNotNull(result);
        assertFalse(result.isEmpty());                        // should not be empty
        assertTrue(result.get(0).isAvailable());              // item should be available
        assertEquals("Chicken Biryani", result.get(0).getName());
    }

    // =========================================================
    //  TEST 8 — getAllAvailableItems() empty case
    // =========================================================
    @Test
    void testGetAllAvailableItems_WhenNoneAvailable_ShouldReturnEmptyList() {
        // ARRANGE
        when(menuItemRepository.findAllAvailable())
                .thenReturn(Collections.emptyList());

        // ACT
        List<MenuResponseDto> result = menuService.getAllAvailableItems();

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =========================================================
    //  TEST 9 — updateAvailability() — mark as unavailable
    // =========================================================
    @Test
    void testUpdateAvailability_ShouldMarkUnavailable() {
        // ARRANGE
        doNothing().when(menuItemRepository).updateAvailability(anyLong(), anyBoolean());

        // ACT
        menuService.updateAvailability(1L, false);

        // ASSERT
        verify(menuItemRepository, times(1)).updateAvailability(1L, false);
    }

    // =========================================================
    //  TEST 10 — updateAvailability() — mark as available
    // =========================================================
    @Test
    void testUpdateAvailability_ShouldMarkAvailable() {
        // ARRANGE
        doNothing().when(menuItemRepository).updateAvailability(anyLong(), anyBoolean());

        // ACT
        menuService.updateAvailability(1L, true);

        // ASSERT
        verify(menuItemRepository, times(1)).updateAvailability(1L, true);
    }
}
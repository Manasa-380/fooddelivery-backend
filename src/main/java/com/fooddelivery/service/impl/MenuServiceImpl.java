package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.MenuRequestDto;
import com.fooddelivery.dto.response.MenuResponseDto;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.exception.MenuItemNotFoundException;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Menu operations.
 * Contains business logic for adding, updating, deleting
 * and retrieving menu items.
 * Converts between MenuItem entity and MenuResponseDto.
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    /**
     * Adds a new menu item to a restaurant's menu.
     * Converts MenuRequestDto to MenuItem entity and saves to database.
     * New items are set as available by default.
     * Throws InvalidRequestException if name, price or restaurantId is invalid.
     *
     * @param dto contains item name, description, price and restaurantId
     */
    @Override
    public void addMenuItem(MenuRequestDto dto) {
        // ✅ validate name
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Item name cannot be empty.");
        }
        // ✅ validate price
        if (dto.getPrice() == null || dto.getPrice().doubleValue() <= 0) {
            throw new InvalidRequestException("Price must be greater than zero.");
        }
        // ✅ validate restaurantId
        if (dto.getRestaurantId() == null) {
            throw new InvalidRequestException("Restaurant ID is required.");
        }

        MenuItem item = new MenuItem();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setRestaurantId(dto.getRestaurantId());
        item.setAvailable(true);
        menuItemRepository.save(item);
    }

    /**
     * Updates an existing menu item with new details.
     * Converts MenuRequestDto to MenuItem entity and updates in database.
     * Throws InvalidRequestException if ID or fields are invalid.
     *
     * @param itemId the ID of the item to update
     * @param dto    contains the new name, description and price
     */
    @Override
    public void updateMenuItem(Long itemId, MenuRequestDto dto) {
        // ✅ validate itemId
        if (itemId == null || itemId <= 0) {
            throw new InvalidRequestException("Invalid item ID.");
        }
        // ✅ validate name
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Item name cannot be empty.");
        }
        // ✅ validate price
        if (dto.getPrice() == null || dto.getPrice().doubleValue() <= 0) {
            throw new InvalidRequestException("Price must be greater than zero.");
        }

        MenuItem item = new MenuItem();
        item.setItemId(itemId);
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setRestaurantId(dto.getRestaurantId());
        menuItemRepository.update(item);
    }

    /**
     * Permanently deletes a menu item from the database.
     * Throws InvalidRequestException if ID is invalid.
     *
     * @param itemId the ID of the menu item to delete
     */
    @Override
    public void deleteMenuItem(Long itemId) {
        // ✅ validate itemId
        if (itemId == null || itemId <= 0) {
            throw new InvalidRequestException("Invalid item ID.");
        }
        menuItemRepository.delete(itemId);
    }

    /**
     * Retrieves all menu items for a specific restaurant.
     * Converts each MenuItem entity to MenuResponseDto before returning.
     * Throws InvalidRequestException if restaurantId is invalid.
     *
     * @param restaurantId the ID of the restaurant
     * @return list of MenuResponseDto objects for that restaurant
     */
    @Override
    public List<MenuResponseDto> getMenuByRestaurant(Long restaurantId) {
        // ✅ validate restaurantId
        if (restaurantId == null || restaurantId <= 0) {
            throw new InvalidRequestException("Invalid restaurant ID.");
        }
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single menu item by its ID.
     * Converts MenuItem entity to MenuResponseDto before returning.
     * Throws InvalidRequestException if ID is invalid.
     * Throws MenuItemNotFoundException if item does not exist.
     *
     * @param itemId the ID of the menu item
     * @return the menu item as MenuResponseDto
     */
    @Override
    public MenuResponseDto getMenuItemById(Long itemId) {
        // ✅ validate itemId
        if (itemId == null || itemId <= 0) {
            throw new InvalidRequestException("Invalid item ID.");
        }
        // ✅ check if item exists
        MenuItem item = menuItemRepository.findById(itemId);
        if (item == null) {
            throw new MenuItemNotFoundException(
                    "Menu item not found with ID: " + itemId
            );
        }
        return convertToDto(item);
    }

    /**
     * Retrieves all available menu items across all restaurants.
     * Only returns items where is_available is true AND quantity greater than 0.
     * Throws MenuItemNotFoundException if no items are available.
     *
     * @return list of all available items as MenuResponseDto
     */
    @Override
    public List<MenuResponseDto> getAllAvailableItems() {
        List<MenuItem> items = menuItemRepository.findAllAvailable();
        // ✅ check if list is empty
        if (items == null || items.isEmpty()) {
            throw new MenuItemNotFoundException(
                    "No available menu items found."
            );
        }
        return items.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates the availability status of a specific menu item.
     * Restaurant owner can toggle item ON (true) or OFF (false).
     * Throws InvalidRequestException if ID is invalid.
     *
     * @param itemId the ID of the menu item
     * @param status true to mark available, false to mark unavailable
     */
    @Override
    public void updateAvailability(Long itemId, boolean status) {
        // ✅ validate itemId
        if (itemId == null || itemId <= 0) {
            throw new InvalidRequestException("Invalid item ID.");
        }
        menuItemRepository.updateAvailability(itemId, status);
    }

    /**
     * Private helper method that converts a MenuItem entity to MenuResponseDto.
     * Used by all methods that return data to avoid exposing raw entity.
     *
     * @param item the MenuItem entity from database
     * @return MenuResponseDto with fields copied from MenuItem
     */
    private MenuResponseDto convertToDto(MenuItem item) {
        MenuResponseDto dto = new MenuResponseDto();
        dto.setItemId(item.getItemId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setRestaurantId(item.getRestaurantId());
        dto.setAvailable(item.isAvailable());
        return dto;
    }
}
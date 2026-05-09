package com.fooddelivery.service;

import com.fooddelivery.dto.request.MenuRequestDto;
import com.fooddelivery.dto.response.MenuResponseDto;
import java.util.List;

public interface MenuService {
    void addMenuItem(MenuRequestDto dto);
    void updateMenuItem(Long itemId, MenuRequestDto dto);
    void deleteMenuItem(Long itemId);
    List<MenuResponseDto> getMenuByRestaurant(Long restaurantId);
    MenuResponseDto getMenuItemById(Long itemId);
    List<MenuResponseDto> getAllAvailableItems();
    void updateAvailability(Long itemId, boolean status);
}
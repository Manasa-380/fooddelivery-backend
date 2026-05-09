package com.service.impl;

import com.dto.request.MenuRequestDto;
import com.dto.response.MenuResponseDto;
import com.entity.MenuItem;
import com.repository.MenuItemRepository;
import com.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public void addMenuItem(MenuRequestDto dto) {
        MenuItem item = new MenuItem();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setRestaurantId(dto.getRestaurantId());
        item.setAvailable(true);
        menuItemRepository.save(item);
    }

    @Override
    public void updateMenuItem(Long itemId, MenuRequestDto dto) {
        MenuItem item = new MenuItem();
        item.setItemId(itemId);
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setRestaurantId(dto.getRestaurantId());
        menuItemRepository.update(item);
    }

    @Override
    public void deleteMenuItem(Long itemId) {
        menuItemRepository.delete(itemId);
    }

    @Override
    public List<MenuResponseDto> getMenuByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MenuResponseDto getMenuItemById(Long itemId) {
        return convertToDto(menuItemRepository.findById(itemId));
    }

    @Override
    public List<MenuResponseDto> getAllAvailableItems() {
        return menuItemRepository.findAllAvailable()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateAvailability(Long itemId, boolean status) {
        menuItemRepository.updateAvailability(itemId, status);
    }

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
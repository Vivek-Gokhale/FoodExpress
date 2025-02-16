package com.foodexpress;

import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.admin.model.RestaurantMenuItem;
import com.foodexpress.admin.service.MenuItemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MenuItemServiceTest {

    private MenuItemService menuItemService;
    private MenuItemDao menuItemDao;

    @BeforeEach
    void setUp() {
        menuItemDao = Mockito.mock(MenuItemDao.class);
        menuItemService = new MenuItemService();
        menuItemService.menuItemDao = menuItemDao;
    }

    @Test
    void testAddMenuItem() {
        RestaurantMenuItem item = new RestaurantMenuItem(1, "Burger", 200, 1, null, null, 0);
        Mockito.when(menuItemDao.save(any(RestaurantMenuItem.class))).thenReturn(item);

        Optional<RestaurantMenuItem> result = menuItemService.addMenuItem(item);

        assertTrue(result.isPresent());
        assertEquals(item.getItemId(), result.get().getItemId());
        Mockito.verify(menuItemDao, Mockito.times(1)).save(item);
    }

    @Test
    void testUpdateMenuItem_Exists() {
        RestaurantMenuItem item = new RestaurantMenuItem(1, "Pizza", 300, 1, null, null, 0);

        Mockito.when(menuItemDao.existsById(item.getItemId())).thenReturn(true);
        Mockito.when(menuItemDao.save(any(RestaurantMenuItem.class))).thenReturn(item);

        Optional<RestaurantMenuItem> result = menuItemService.updateMenuItem(item);

        assertTrue(result.isPresent());
        assertEquals(item.getItemId(), result.get().getItemId());
        Mockito.verify(menuItemDao, Mockito.times(1)).existsById(item.getItemId());
        Mockito.verify(menuItemDao, Mockito.times(1)).save(item);
    }

    @Test
    void testUpdateMenuItem_NotExists() {
        RestaurantMenuItem item = new RestaurantMenuItem(1, "Sandwich", 150, 1, null, null, 0);

        Mockito.when(menuItemDao.existsById(item.getItemId())).thenReturn(false);

        Optional<RestaurantMenuItem> result = menuItemService.updateMenuItem(item);

        assertFalse(result.isPresent());
        Mockito.verify(menuItemDao, Mockito.times(1)).existsById(item.getItemId());
        Mockito.verify(menuItemDao, Mockito.never()).save(any(RestaurantMenuItem.class));
    }

    @Test
    void testDeleteMenuItem_Exists() {
        int menuItemId = 1;

        Mockito.when(menuItemDao.existsById(menuItemId)).thenReturn(true);

        boolean result = menuItemService.deleteMenuItem(menuItemId);

        assertTrue(result);
        Mockito.verify(menuItemDao, Mockito.times(1)).existsById(menuItemId);
        Mockito.verify(menuItemDao, Mockito.times(1)).deleteById(menuItemId);
    }

    @Test
    void testDeleteMenuItem_NotExists() {
        int menuItemId = 1;

        Mockito.when(menuItemDao.existsById(menuItemId)).thenReturn(false);

        boolean result = menuItemService.deleteMenuItem(menuItemId);

        assertFalse(result);
        Mockito.verify(menuItemDao, Mockito.times(1)).existsById(menuItemId);
        Mockito.verify(menuItemDao, Mockito.never()).deleteById(menuItemId);
    }

    @Test
    void testGetMenuItems() {
        int restaurantId = 1;

        RestaurantMenuItem item1 = new RestaurantMenuItem(1, "Pasta", 250, restaurantId, null, null, restaurantId);
        RestaurantMenuItem item2 = new RestaurantMenuItem(2, "Salad", 150, restaurantId, null, null, restaurantId);

        Mockito.when(menuItemDao.findAllByRestaurantId(eq(restaurantId))).thenReturn(Arrays.asList(item1, item2));

        List<RestaurantMenuItem> result = menuItemService.getMenuItems(restaurantId);

        assertEquals(2, result.size());
        assertEquals(item1.getItemId(), result.get(0).getItemId());
        assertEquals(item2.getItemId(), result.get(1).getItemId());
        Mockito.verify(menuItemDao, Mockito.times(1)).findAllByRestaurantId(restaurantId);
    }

    @Test
    void testGetMenuItem() {
        int itemId = 1;
        RestaurantMenuItem item = new RestaurantMenuItem(itemId, "Noodles", 200, 1, null, null, itemId);

        Mockito.when(menuItemDao.findById(eq(itemId))).thenReturn(Optional.of(item));

        Optional<RestaurantMenuItem> result = menuItemService.getMenuItem(itemId);

        assertTrue(result.isPresent());
        assertEquals(item.getItemId(), result.get().getItemId());
        Mockito.verify(menuItemDao, Mockito.times(1)).findById(itemId);
    }
}

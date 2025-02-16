package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.admin.dao.OrderItemDao;
import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.admin.dao.RestaurantRegisterDao;
import com.foodexpress.admin.dto.CustomerMenuPreferenceStats;
import com.foodexpress.admin.model.OrderItem;
import com.foodexpress.admin.model.RestaurantMenuItem;
import com.foodexpress.admin.model.RestaurantRegister;
import com.foodexpress.admin.service.OrderItemService;

class OrderItemServiceTest {

    @InjectMocks
    private OrderItemService orderItemService;

    @Mock
    private OrderItemDao orderItemDao;

    @Mock
    private MenuItemDao menuItemDao;

    @Mock
    private RestaurantRegisterDao restaurantRegisterDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderItems() {
        // Mock data
        OrderItem orderItem = new OrderItem();
        orderItem.setRestaurantId(1);
        orderItem.setItemId(101);

        RestaurantRegister restaurant = new RestaurantRegister();
        restaurant.setName("Test Restaurant");

        RestaurantMenuItem menuItem = new RestaurantMenuItem();
        menuItem.setName("Test Dish");

        // Mock behavior
        when(orderItemDao.findByRestaurantId(1)).thenReturn(Arrays.asList(orderItem));
        when(restaurantRegisterDao.findById(1)).thenReturn(Optional.of(restaurant));
        when(menuItemDao.findById(101)).thenReturn(Optional.of(menuItem));

        // Call the method
        List<OrderItem> result = orderItemService.getOrderItems(1);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Restaurant", result.get(0).getRestaurantName());
        assertEquals("Test Dish", result.get(0).getItemName());
    }

    @Test
    void testUpdateOrderStatus() {
        // Mock data
        OrderItem orderItem = new OrderItem();

        // Mock behavior
        when(orderItemDao.save(orderItem)).thenReturn(orderItem);

        // Call the method
        boolean result = orderItemService.updateOrderStatus(orderItem);

        // Assertions
        assertTrue(result);
        verify(orderItemDao, times(1)).save(orderItem);
    }

    @Test
    void testGetMenuPreferencesForRestaurant() {
        // Mock data
        Object[] row1 = { "Dish 1", 10L, "Spicy, Extra Cheese" };
        Object[] row2 = { "Dish 2", 20L, "Less Salt, No Onion" };

        // Mock behavior
        when(orderItemDao.findCustomerMenuPreferenceStatsByRestaurantId(1))
                .thenReturn(Arrays.asList(row1, row2));

        // Call the method
        List<CustomerMenuPreferenceStats> stats = orderItemService.getMenuPreferencesForRestaurant(1);

        // Assertions
        assertNotNull(stats);
        assertEquals(2, stats.size());

        CustomerMenuPreferenceStats stat1 = stats.get(0);
        assertEquals("Dish 1", stat1.getDishName());
        assertEquals(10L, stat1.getOrderCount());
        assertEquals(Arrays.asList("Spicy", "Extra Cheese"), stat1.getCustomizations());
        assertEquals(33.33, stat1.getPercentageOfTotalOrders(), 0.01);

        CustomerMenuPreferenceStats stat2 = stats.get(1);
        assertEquals("Dish 2", stat2.getDishName());
        assertEquals(20L, stat2.getOrderCount());
        assertEquals(Arrays.asList("Less Salt", "No Onion"), stat2.getCustomizations());
        assertEquals(66.67, stat2.getPercentageOfTotalOrders(), 0.01);
    }
}

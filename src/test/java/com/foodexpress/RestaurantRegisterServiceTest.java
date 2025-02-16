package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.admin.dao.RestaurantRegisterDao;
import com.foodexpress.admin.model.RestaurantRegister;
import com.foodexpress.admin.service.RestaurantRegisterService;

class RestaurantRegisterServiceTest {

    @Mock
    private RestaurantRegisterDao restaurantRegisterDao;

    @InjectMocks
    private RestaurantRegisterService restaurantRegisterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterRestaurant() {
        RestaurantRegister restaurant = new RestaurantRegister();
        restaurant.setRestaurantId(1);
        restaurant.setName("Test Restaurant");

        when(restaurantRegisterDao.save(restaurant)).thenReturn(restaurant);

        int restaurantId = restaurantRegisterService.registerRestaurant(restaurant);

        assertEquals(1, restaurantId);
        verify(restaurantRegisterDao, times(1)).save(restaurant);
    }

    @Test
    void testUpdateRestaurant_Success() {
        RestaurantRegister restaurant = new RestaurantRegister();
        restaurant.setRestaurantId(1);
        restaurant.setName("Updated Restaurant");

        when(restaurantRegisterDao.existsById(1)).thenReturn(true);

        boolean result = restaurantRegisterService.updateRestaurant(restaurant);

        assertTrue(result);
        verify(restaurantRegisterDao, times(1)).existsById(1);
        verify(restaurantRegisterDao, times(1)).save(restaurant);
    }

    @Test
    void testUpdateRestaurant_Failure() {
        RestaurantRegister restaurant = new RestaurantRegister();
        restaurant.setRestaurantId(1);
        restaurant.setName("Non-existent Restaurant");

        when(restaurantRegisterDao.existsById(1)).thenReturn(false);

        boolean result = restaurantRegisterService.updateRestaurant(restaurant);

        assertFalse(result);
        verify(restaurantRegisterDao, times(1)).existsById(1);
        verify(restaurantRegisterDao, never()).save(any(RestaurantRegister.class));
    }

    @Test
    void testGetRestaurant_Success() {
        RestaurantRegister restaurant = new RestaurantRegister();
        restaurant.setRestaurantId(1);
        restaurant.setName("Test Restaurant");

        when(restaurantRegisterDao.findById(1)).thenReturn(Optional.of(restaurant));

        RestaurantRegister result = restaurantRegisterService.getRestaurant(1);

        assertNotNull(result);
        assertEquals("Test Restaurant", result.getName());
        verify(restaurantRegisterDao, times(1)).findById(1);
    }

    @Test
    void testGetRestaurant_Failure() {
        when(restaurantRegisterDao.findById(1)).thenReturn(Optional.empty());

        RestaurantRegister result = restaurantRegisterService.getRestaurant(1);

        assertNull(result);
        verify(restaurantRegisterDao, times(1)).findById(1);
    }

    @Test
    void testGetRestaurantId_Success() {
        RestaurantRegister restaurant = new RestaurantRegister();
        restaurant.setRestaurantId(1);
        restaurant.setEmail("test@example.com");

        when(restaurantRegisterDao.findRestaurantIdByEmail("test@example.com")).thenReturn(Optional.of(restaurant));

        int restaurantId = restaurantRegisterService.getRestaurantId("test@example.com");

        assertEquals(1, restaurantId);
        verify(restaurantRegisterDao, times(1)).findRestaurantIdByEmail("test@example.com");
    }

    @Test
    void testGetRestaurantId_Failure() {
        when(restaurantRegisterDao.findRestaurantIdByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(NullPointerException.class, () -> {
            restaurantRegisterService.getRestaurantId("unknown@example.com");
        });

        assertNotNull(exception);
        verify(restaurantRegisterDao, times(1)).findRestaurantIdByEmail("unknown@example.com");
    }
}

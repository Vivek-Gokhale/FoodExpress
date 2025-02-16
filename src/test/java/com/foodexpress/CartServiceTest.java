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

import com.foodexpress.customer.dao.CartDao;
import com.foodexpress.customer.model.Cart;
import com.foodexpress.customer.service.CartService;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartDao cartDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart_NewItem() {
        // Mock data
        Cart newCartItem = new Cart();
        newCartItem.setUserId(1);
        newCartItem.setItemId(101);
        newCartItem.setQuantity(2);
        newCartItem.setStocks(10);

        // Mock behavior (Item does not exist in cart)
        when(cartDao.findByUserIdAndItemId(1, 101)).thenReturn(null);
        when(cartDao.save(newCartItem)).thenReturn(newCartItem);
        when(cartDao.findByUserId(1)).thenReturn(Arrays.asList(newCartItem));

        // Call the method
        List<Cart> result = cartService.addToCart(newCartItem);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getQuantity());
        verify(cartDao, times(1)).save(newCartItem);
        verify(cartDao, times(1)).findByUserId(1);
    }

    @Test
    void testAddToCart_ExistingItem() {
        // Mock existing cart item
        Cart existingCartItem = new Cart();
        existingCartItem.setUserId(1);
        existingCartItem.setItemId(101);
        existingCartItem.setQuantity(2);
        existingCartItem.setStocks(10);

        // New cart item to add
        Cart newCartItem = new Cart();
        newCartItem.setUserId(1);
        newCartItem.setItemId(101);
        newCartItem.setQuantity(3);
        newCartItem.setStocks(10);

        // Mock behavior (Item already exists in cart)
        when(cartDao.findByUserIdAndItemId(1, 101)).thenReturn(existingCartItem);
        when(cartDao.save(any(Cart.class))).thenReturn(existingCartItem);
        when(cartDao.findByUserId(1)).thenReturn(Arrays.asList(existingCartItem));

        // Call the method
        List<Cart> result = cartService.addToCart(newCartItem);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getQuantity()); // 2 + 3 = 5
        verify(cartDao, times(1)).save(any(Cart.class));
        verify(cartDao, times(1)).findByUserId(1);
    }

    @Test
    void testUpdateCart_IncreaseQuantity() {
        // Mock existing cart item
        Cart existingCartItem = new Cart();
        existingCartItem.setUserId(1);
        existingCartItem.setItemId(101);
        existingCartItem.setQuantity(2);
        existingCartItem.setStocks(10);

        // Mock behavior
        when(cartDao.findByUserIdAndItemId(1, 101)).thenReturn(existingCartItem);
        when(cartDao.save(any(Cart.class))).thenReturn(existingCartItem);
        when(cartDao.findByUserId(1)).thenReturn(Arrays.asList(existingCartItem));

        // Call the method with flag 1 (increase quantity)
        List<Cart> result = cartService.updateCart(existingCartItem, 1);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getQuantity()); // Increased by 1
        verify(cartDao, times(1)).save(any(Cart.class));
        verify(cartDao, times(1)).findByUserId(1);
    }

    @Test
    void testUpdateCart_DecreaseQuantity() {
        // Mock existing cart item
        Cart existingCartItem = new Cart();
        existingCartItem.setUserId(1);
        existingCartItem.setItemId(101);
        existingCartItem.setQuantity(3);
        existingCartItem.setStocks(10);

        // Mock behavior
        when(cartDao.findByUserIdAndItemId(1, 101)).thenReturn(existingCartItem);
        when(cartDao.save(any(Cart.class))).thenReturn(existingCartItem);
        when(cartDao.findByUserId(1)).thenReturn(Arrays.asList(existingCartItem));

        // Call the method with flag 0 (decrease quantity)
        List<Cart> result = cartService.updateCart(existingCartItem, 0);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getQuantity()); // Decreased by 1
        verify(cartDao, times(1)).save(any(Cart.class));
        verify(cartDao, times(1)).findByUserId(1);
    }

    @Test
    void testRemoveFromCart() {
        // Mock behavior
        when(cartDao.findByUserId(1)).thenReturn(Arrays.asList());

        // Call the method
        List<Cart> result = cartService.removeFromCart(101, 1);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Should be empty after removing item
        verify(cartDao, times(1)).deleteById(101);
        verify(cartDao, times(1)).findByUserId(1);
    }
}

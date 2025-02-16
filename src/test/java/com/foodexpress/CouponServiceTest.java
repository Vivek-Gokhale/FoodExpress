package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodexpress.customer.dao.CouponDao;
import com.foodexpress.customer.dao.OrderDao;
import com.foodexpress.customer.model.Coupon;
import com.foodexpress.customer.model.Order;
import com.foodexpress.customer.service.CouponService;

class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponDao couponDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetValidCoupons_WhenUserHasNoOrders() {
        // Mock Data
        Coupon coupon1 = new Coupon();
        coupon1.setCouponId(101);

        Coupon coupon2 = new Coupon();
        coupon2.setCouponId(102);

        List<Coupon> allCoupons = Arrays.asList(coupon1, coupon2);

        // Mock behavior
        when(couponDao.findAll()).thenReturn(allCoupons);
        when(orderDao.findByUserId(1)).thenReturn(Collections.emptyList()); // No orders for user

        // Call the method
        List<Coupon> result = couponService.getValidCoupons(1);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size()); // All coupons should be valid
        verify(couponDao, times(1)).findAll();
        verify(orderDao, times(1)).findByUserId(1);
    }

    @Test
    void testGetValidCoupons_WhenUserHasUsedCoupons() throws JsonProcessingException {
        // Mock Data
        Coupon coupon1 = new Coupon();
        coupon1.setCouponId(101);

        Coupon coupon2 = new Coupon();
        coupon2.setCouponId(102);

        List<Coupon> allCoupons = Arrays.asList(coupon1, coupon2);

        Order order1 = new Order();
        order1.setCouponIds("[101]"); // User has used coupon 101

        List<Order> allOrders = Arrays.asList(order1);

        // Mock behavior
        when(couponDao.findAll()).thenReturn(allCoupons);
        when(orderDao.findByUserId(1)).thenReturn(allOrders);
        when(objectMapper.readValue("[101]", List.class)).thenReturn(Arrays.asList(101));

        // Call the method
        List<Coupon> result = couponService.getValidCoupons(1);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size()); // Only coupon 102 should be valid
        assertEquals(102, result.get(0).getCouponId());
        verify(couponDao, times(1)).findAll();
        verify(orderDao, times(1)).findByUserId(1);
    }

    @Test
    void testGetValidCoupons_WhenUserHasUsedAllCoupons() throws JsonProcessingException {
        // Mock Data
        Coupon coupon1 = new Coupon();
        coupon1.setCouponId(101);

        Coupon coupon2 = new Coupon();
        coupon2.setCouponId(102);

        List<Coupon> allCoupons = Arrays.asList(coupon1, coupon2);

        Order order1 = new Order();
        order1.setCouponIds("[101, 102]"); // User has used both coupons

        List<Order> allOrders = Arrays.asList(order1);

        // Mock behavior
        when(couponDao.findAll()).thenReturn(allCoupons);
        when(orderDao.findByUserId(1)).thenReturn(allOrders);
        when(objectMapper.readValue("[101, 102]", List.class)).thenReturn(Arrays.asList(101, 102));

        // Call the method
        List<Coupon> result = couponService.getValidCoupons(1);

        // Assertions
        assertNotNull(result);
        assertEquals(0, result.size()); // No valid coupons left
        verify(couponDao, times(1)).findAll();
        verify(orderDao, times(1)).findByUserId(1);
    }
}

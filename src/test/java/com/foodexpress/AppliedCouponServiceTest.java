package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.admin.model.RestaurantMenuItem;
import com.foodexpress.customer.dao.AppliedCouponDao;
import com.foodexpress.customer.dao.CartDao;
import com.foodexpress.customer.dao.CouponDao;
import com.foodexpress.customer.dto.OrderSummary;
import com.foodexpress.customer.model.AppliedCoupon;
import com.foodexpress.customer.model.Cart;
import com.foodexpress.customer.model.Coupon;
import com.foodexpress.customer.service.AppliedCouponService;

class AppliedCouponServiceTest {

    @InjectMocks
    private AppliedCouponService appliedCouponService;

    @Mock
    private AppliedCouponDao appliedCouponDao;

    @Mock
    private CartDao cartDao;

    @Mock
    private MenuItemDao menuItemDao;

    @Mock
    private CouponDao couponDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplyCoupon_Success() {
        // Mock data
        int userId = 1;
        int couponId = 101;

        Coupon coupon = new Coupon();
        coupon.setCouponId(couponId);
        coupon.setMinOrderValue(100);
        coupon.setUsageCount(5);

        Cart cart = new Cart();
        cart.setItemId(1);
        cart.setQuantity(2);

        RestaurantMenuItem menuItem = new RestaurantMenuItem();
        menuItem.setPrice(60); // Price per item

        // Mock behavior
        when(couponDao.findById(couponId)).thenReturn(Optional.of(coupon));
        when(cartDao.findByUserId(userId)).thenReturn(List.of(cart));
        when(menuItemDao.findById(cart.getItemId())).thenReturn(Optional.of(menuItem));
        when(appliedCouponDao.save(any(AppliedCoupon.class))).thenReturn(new AppliedCoupon());

        // Call method
        int result = appliedCouponService.applyCoupon(couponId, userId);

        // Assertions
        assertEquals(1, result);
        verify(appliedCouponDao, times(1)).save(any(AppliedCoupon.class));
    }

    @Test
    void testApplyCoupon_MinOrderNotSatisfied() {
        int userId = 1;
        int couponId = 101;

        Coupon coupon = new Coupon();
        coupon.setMinOrderValue(200); // Higher than cart total
        coupon.setUsageCount(5);

        Cart cart = new Cart();
        cart.setItemId(1);
        cart.setQuantity(2);

        RestaurantMenuItem menuItem = new RestaurantMenuItem();
        menuItem.setPrice(60);

        // Mock behavior
        when(couponDao.findById(couponId)).thenReturn(Optional.of(coupon));
        when(cartDao.findByUserId(userId)).thenReturn(List.of(cart));
        when(menuItemDao.findById(cart.getItemId())).thenReturn(Optional.of(menuItem));

        // Call method
        int result = appliedCouponService.applyCoupon(couponId, userId);

        // Assertions
        assertEquals(2, result);
        verify(appliedCouponDao, never()).save(any());
    }

    @Test
    void testApplyCoupon_UsageCountExpired() {
        int userId = 1;
        int couponId = 101;

        Coupon coupon = new Coupon();
        coupon.setMinOrderValue(100);
        coupon.setUsageCount(0); // Expired usage count

        Cart cart = new Cart();
        cart.setItemId(1);
        cart.setQuantity(2);

        RestaurantMenuItem menuItem = new RestaurantMenuItem();
        menuItem.setPrice(60);

        // Mock behavior
        when(couponDao.findById(couponId)).thenReturn(Optional.of(coupon));
        when(cartDao.findByUserId(userId)).thenReturn(List.of(cart));
        when(menuItemDao.findById(cart.getItemId())).thenReturn(Optional.of(menuItem));

        // Call method
        int result = appliedCouponService.applyCoupon(couponId, userId);

        // Assertions
        assertEquals(3, result);
        verify(appliedCouponDao, never()).save(any());
    }

    @Test
    void testGetOrderSummary_Success() {
        int userId = 1;

        AppliedCoupon appliedCoupon = new AppliedCoupon();
        appliedCoupon.setAppliedCouponsId(101);

        Cart cart = new Cart();
        cart.setItemId(1);
        cart.setQuantity(2);

        RestaurantMenuItem menuItem = new RestaurantMenuItem();
        menuItem.setPrice(50);

        Coupon coupon = new Coupon();
        coupon.setCouponId(101);
        coupon.setPercentage(10);
        coupon.setMaxDiscountValue(20);

        // Mock behavior
        when(appliedCouponDao.findByUserId(userId)).thenReturn(List.of(appliedCoupon));
        when(cartDao.findByUserId(userId)).thenReturn(List.of(cart));
        when(menuItemDao.findById(cart.getItemId())).thenReturn(Optional.of(menuItem));
        when(couponDao.findAll()).thenReturn(List.of(coupon));

        // Call method
        OrderSummary summary = appliedCouponService.getOrderSummary(userId);

        // Assertions
        assertNotNull(summary);
        assertEquals(100, summary.getTotalAmount()); // 2 * 50
        assertEquals(10, summary.getCouponDiscount()); // 10% of 100 = 10
        assertEquals(10, summary.getTax()); // 10% tax on 100
        assertEquals(10, summary.getSavings()); // Same as discount
        assertEquals(100 - 10 + 10, summary.getFinalAmount()); // Total - Discount + Tax

        verify(appliedCouponDao, times(1)).findByUserId(userId);
        verify(cartDao, times(1)).findByUserId(userId);
        verify(menuItemDao, times(1)).findById(cart.getItemId());
        verify(couponDao, times(1)).findAll();
    }
}

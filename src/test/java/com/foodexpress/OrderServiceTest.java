package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.admin.dao.OrderItemDao;
import com.foodexpress.admin.dao.RestaurantRegisterDao;
import com.foodexpress.admin.model.RestaurantMenuItem;
import com.foodexpress.customer.dao.AppliedCouponDao;
import com.foodexpress.customer.dao.CartDao;
import com.foodexpress.customer.dao.CustomerDao;
import com.foodexpress.customer.dao.OrderDao;
import com.foodexpress.customer.model.*;
import com.foodexpress.customer.service.OrderService;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderItemDao orderItemDao;

    @Mock
    private MenuItemDao menuItemDao;

    @Mock
    private RestaurantRegisterDao restaurantRegisterDao;

    @Mock
    private AppliedCouponDao appliedCouponDao;

    @Mock
    private CartDao cartDao;

    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrders() {
        // Mock data
        int userId = 1;
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setOrderId(101);
        orders.add(order);

        // Mock DAO behavior
        when(orderDao.findByUserId(userId)).thenReturn(orders);
        when(orderItemDao.findByOrderId(101)).thenReturn(new ArrayList<>());

        // Call the method
        List<Order> result = orderService.getOrders(userId);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderDao, times(1)).findByUserId(userId);
    }

    @Test
    void testPlaceOrder_Success() {
        // Mock data
        int userId = 1;
        int paymentFlag = 1;
        double finalAmount = 100.0;
        Map<Integer, String> itemRequestMap = new HashMap<>();
        itemRequestMap.put(1, "No onions");

        List<Cart> carts = new ArrayList<>();
        Cart cart = new Cart();
        cart.setItemId(1);
        cart.setQuantity(2);
        carts.add(cart);

        List<AppliedCoupon> appliedCoupons = new ArrayList<>();
        AppliedCoupon coupon = new AppliedCoupon();
        coupon.setAppliedCouponsId(1);
        appliedCoupons.add(coupon);

        // Mock DAO behavior
        when(appliedCouponDao.findByUserId(userId)).thenReturn(appliedCoupons);
        when(cartDao.findByUserId(userId)).thenReturn(carts);
        when(menuItemDao.findById(1)).thenReturn(Optional.of(new RestaurantMenuItem()));
        when(orderDao.save(any(Order.class))).thenReturn(new Order());
        when(orderItemDao.saveAll(anyList())).thenReturn(new ArrayList<>());
        when(menuItemDao.saveAll(anyList())).thenReturn(new ArrayList<>());
       
        // Call the method
        Order result = orderService.placeOrder(userId, paymentFlag, finalAmount, itemRequestMap);

        // Assertions
        assertNotNull(result);
        verify(orderDao, times(1)).save(any(Order.class));
        verify(orderItemDao, times(1)).saveAll(anyList());
        verify(menuItemDao, times(1)).saveAll(anyList());
    }

    @Test
    void testPlaceOrder_Failure() {
        // Mock data
        int userId = 1;
        int paymentFlag = 1;
        double finalAmount = 100.0;
        Map<Integer, String> itemRequestMap = new HashMap<>();

        // Mock DAO behavior
        when(appliedCouponDao.findByUserId(userId)).thenReturn(new ArrayList<>());
        when(cartDao.findByUserId(userId)).thenReturn(new ArrayList<>());
        when(orderDao.save(any(Order.class))).thenReturn(null); // Simulate order not saved

        // Call the method
        Order result = orderService.placeOrder(userId, paymentFlag, finalAmount, itemRequestMap);

        // Assertions
        assertNull(result);
        verify(orderDao, times(1)).save(any(Order.class));
    }

    @Test
    void testGetCustomer() {
        // Mock data
        int userId = 1;
        Customer customer = new Customer();
        customer.setUserId(userId);

        // Mock DAO behavior
        when(customerDao.findById(userId)).thenReturn(Optional.of(customer));

        // Call the method
        Customer result = orderService.getCustomer(userId);

        // Assertions
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(customerDao, times(1)).findById(userId);
    }

    @Test
    void testGetDeliveryStatus() {
        // Mock data
        int orderId = 1;
        List<DeliveryPartner> deliveryPartners = new ArrayList<>();
        DeliveryPartner deliveryPartner = new DeliveryPartner();
        deliveryPartners.add(deliveryPartner);

        // Mock DAO behavior
        when(orderDao.getDeliveryPartner(orderId)).thenReturn(deliveryPartners);

        // Call the method
        List<DeliveryPartner> result = orderService.getDeliveryStatus(orderId);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderDao, times(1)).getDeliveryPartner(orderId);
    }
}

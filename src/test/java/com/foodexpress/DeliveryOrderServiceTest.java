package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.customer.dao.CustomerDao;
import com.foodexpress.customer.dao.OrderDao;
import com.foodexpress.customer.model.Customer;
import com.foodexpress.customer.model.Order;
import com.foodexpress.deliveryPartner.dao.DeliveryOrderDao;
import com.foodexpress.deliveryPartner.dao.DeliveryPartnerDao;
import com.foodexpress.deliveryPartner.dto.OrderStatus;
import com.foodexpress.deliveryPartner.model.DeliveryOrder;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;
import com.foodexpress.deliveryPartner.service.DeliveryOrderService;
import com.foodexpress.utilities.EmailService;

class DeliveryOrderServiceTest {

    @InjectMocks
    private DeliveryOrderService deliveryOrderService;

    @Mock
    private DeliveryOrderDao deliveryOrderDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private DeliveryPartnerDao deliveryPartnerDao;

    @Mock
    private EmailService emailService;

    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSelectDeliveryOrder() {
        // Mock data
        Integer partnerId = 1;
        Integer orderId = 1;
        String longitude = "10.123";
        String latitude = "20.456";
        
        // Mock DAO behavior
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        when(deliveryOrderDao.save(any(DeliveryOrder.class))).thenReturn(deliveryOrder);
        
        Order order = new Order();
        when(orderDao.findById(orderId)).thenReturn(Optional.of(order));
        
        DeliveryPartner partner = new DeliveryPartner();
        when(deliveryPartnerDao.findById(partnerId)).thenReturn(Optional.of(partner));

        // Call the method
        boolean result = deliveryOrderService.selectDeliveryOrder(partnerId, orderId, longitude, latitude);

        // Assertions
        assertTrue(result);
        verify(deliveryOrderDao, times(1)).save(any(DeliveryOrder.class));
        verify(orderDao, times(1)).save(order);
        verify(deliveryPartnerDao, times(1)).save(partner);
    }

    @Test
    void testVerifyOTP() {
        // Mock data
        Integer deliveryId = 1;
        Integer userId = 1;
        
        // Mock DAO behavior
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setDeliveryPartnerId(1);
        when(deliveryOrderDao.findById(deliveryId)).thenReturn(Optional.of(deliveryOrder));
        
        DeliveryPartner partner = new DeliveryPartner();
        partner.setDeliveryPartnerId(1);
        when(deliveryPartnerDao.findById(1)).thenReturn(Optional.of(partner));
        
        Order order = new Order();
        when(orderDao.findById(deliveryOrder.getOrderId())).thenReturn(Optional.of(order));

        // Call the method
        boolean result = deliveryOrderService.verifyOTP(deliveryId, userId);

        // Assertions
        assertTrue(result);
        verify(deliveryPartnerDao, times(1)).save(partner);
        verify(deliveryOrderDao, times(1)).save(deliveryOrder);
        verify(orderDao, times(1)).save(order);
    }

    @Test
    void testGetPendingOrders() {
        // Mock data
        Integer partnerId = 1;
        List<Object[]> rawResults = new ArrayList<>();
        Object[] orderData = {1, 1, 1, "User", "1234567890", "Address", 100, "COD"};
        rawResults.add(orderData);
        
        // Mock DAO behavior
        when(deliveryOrderDao.findPendingOrdersByDeliveryPartner(partnerId)).thenReturn(rawResults);

        // Call the method
        List<OrderStatus> result = deliveryOrderService.getPendingOrders(partnerId);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("User", result.get(0).getUserName());
        verify(deliveryOrderDao, times(1)).findPendingOrdersByDeliveryPartner(partnerId);
    }

    @Test
    void testGetCompletedOrders() {
        // Mock data
        Integer partnerId = 1;
        List<Object[]> rawResults = new ArrayList<>();
        Object[] orderData = {1, 1, 1, "User", "1234567890", "Address", 100, "COD"};
        rawResults.add(orderData);
        
        // Mock DAO behavior
        when(deliveryOrderDao.findCompletedOrdersByDeliveryPartner(partnerId)).thenReturn(rawResults);

        // Call the method
        List<OrderStatus> result = deliveryOrderService.getCompletedOrders(partnerId);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("User", result.get(0).getUserName());
        verify(deliveryOrderDao, times(1)).findCompletedOrdersByDeliveryPartner(partnerId);
    }

    @Test
    void testGetCustomer() {
        // Mock data
        Integer userId = 1;
        Customer customer = new Customer();
        when(customerDao.findById(userId)).thenReturn(Optional.of(customer));

        // Call the method
        Customer result = deliveryOrderService.getCustomer(userId);

        // Assertions
        assertNotNull(result);
        verify(customerDao, times(1)).findById(userId);
    }

    @Test
    void testGetCustomerByOrderId() {
        // Mock data
        Integer orderId = 1;
        Integer userId = 1;
        Order order = new Order();
        order.setUserId(userId);
        when(orderDao.findById(orderId)).thenReturn(Optional.of(order));

        Customer customer = new Customer();
        when(customerDao.findById(userId)).thenReturn(Optional.of(customer));

        // Call the method
        Customer result = deliveryOrderService.getCustomerByOrderId(orderId);

        // Assertions
        assertNotNull(result);
        verify(orderDao, times(1)).findById(orderId);
        verify(customerDao, times(1)).findById(userId);
    }
}

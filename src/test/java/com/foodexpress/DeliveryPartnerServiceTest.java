package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.customer.dao.CustomerAddressDao;
import com.foodexpress.customer.dao.OrderDao;
import com.foodexpress.customer.model.CustomerAddress;
import com.foodexpress.customer.model.Order;
import com.foodexpress.deliveryPartner.dao.DeliveryPartnerDao;
import com.foodexpress.deliveryPartner.dto.PendingOrderStatus;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;
import com.foodexpress.deliveryPartner.service.DeliveryPartnerService;
import com.foodexpress.utilities.PasswordUtils;

class DeliveryPartnerServiceTest {

    @InjectMocks
    private DeliveryPartnerService deliveryPartnerService;

    @Mock
    private DeliveryPartnerDao deliveryPartnerDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private CustomerAddressDao customerAddressDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateLocation() {
        // Mock data
        Integer partnerId = 1;
        String longitude = "10.123";
        String latitude = "20.456";

        // Mock DAO behavior
        DeliveryPartner partner = new DeliveryPartner();
        when(deliveryPartnerDao.findById(partnerId)).thenReturn(Optional.of(partner));

        // Call the method
        boolean result = deliveryPartnerService.updateLocation(partnerId, longitude, latitude);

        // Assertions
        assertTrue(result);
        assertEquals(longitude, partner.getCurrLocLongitude());
        assertEquals(latitude, partner.getCurrLocLatitude());
        verify(deliveryPartnerDao, times(1)).save(partner);
    }

    @Test
    void testRegisterDeliveryPartner() {
        // Mock data
        DeliveryPartner partner = new DeliveryPartner();
        partner.setEmail("partner@foodexpress.com");
        partner.setPassword("password123");

        // Mock DAO behavior
        when(deliveryPartnerDao.findByEmail(partner.getEmail())).thenReturn(null); // No existing partner

        // Call the method
        DeliveryPartner result = deliveryPartnerService.registerDeliveryPartner(partner);

        // Assertions
        assertNotNull(result);
        assertEquals(partner.getEmail(), result.getEmail());
        assertNotEquals("password123", result.getPassword());  // Ensure password is hashed
        verify(deliveryPartnerDao, times(1)).save(partner);
    }

    @Test
    void testRegisterDeliveryPartnerWithExistingEmail() {
        // Mock data
        DeliveryPartner partner = new DeliveryPartner();
        partner.setEmail("partner@foodexpress.com");
        partner.setPassword("password123");

        // Mock DAO behavior
        when(deliveryPartnerDao.findByEmail(partner.getEmail())).thenReturn(partner); // Email already exists

        // Call the method
        DeliveryPartner result = deliveryPartnerService.registerDeliveryPartner(partner);

        // Assertions
        assertNull(result);
        verify(deliveryPartnerDao, times(0)).save(partner); // Should not save if email exists
    }

    @Test
    void testAuthenticateDeliveryPartner() {
        // Mock data
        String email = "partner@foodexpress.com";
        String password = "password123";
        String hashedPassword = PasswordUtils.hashPassword(password);

        DeliveryPartner partner = new DeliveryPartner();
        partner.setEmail(email);
        partner.setPassword(hashedPassword);

        // Mock DAO behavior
        when(deliveryPartnerDao.findByEmailAndPassword(email, hashedPassword)).thenReturn(partner);

        // Call the method
        DeliveryPartner result = deliveryPartnerService.authenticateDeliveryPartner(email, password);

        // Assertions
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(deliveryPartnerDao, times(1)).findByEmailAndPassword(email, hashedPassword);
    }

    @Test
    void testAuthenticateDeliveryPartnerWithInvalidCredentials() {
        // Mock data
        String email = "partner@foodexpress.com";
        String password = "wrongpassword";

        // Mock DAO behavior
        when(deliveryPartnerDao.findByEmailAndPassword(email, PasswordUtils.hashPassword(password))).thenReturn(null);

        // Call the method
        DeliveryPartner result = deliveryPartnerService.authenticateDeliveryPartner(email, password);

        // Assertions
        assertNull(result);
        verify(deliveryPartnerDao, times(1)).findByEmailAndPassword(email, PasswordUtils.hashPassword(password));
    }

    @Test
    void testGetPendingOrders() {
        // Mock data
        Order order1 = new Order();
        order1.setOrderId(1);
        order1.setUserId(1);
        order1.setDeliveryStatus("pending");

        Order order2 = new Order();
        order2.setOrderId(2);
        order2.setUserId(2);
        order2.setDeliveryStatus("completed");

        List<Order> orders = Arrays.asList(order1, order2);

        CustomerAddress address1 = new CustomerAddress();
        address1.setUserId(1);
        address1.setDefault(true);
        address1.setHouseApartment("House 1");
        address1.setArea("Area 1");
        address1.setLandmark("Landmark 1");
        address1.setTownCity("City 1");
        address1.setState("State 1");
        address1.setPincode("123456");

        List<CustomerAddress> addresses = Arrays.asList(address1);

        // Mock DAO behavior
        when(orderDao.findAll()).thenReturn(orders);
        when(customerAddressDao.findAllByUserId(1)).thenReturn(addresses);

        // Call the method
        List<PendingOrderStatus> result = deliveryPartnerService.getPendingOrders();

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size()); // Only one pending order
        assertEquals("House 1, Area 1, Landmark 1, City 1, State 1, 123456", result.get(0).getAddress());
    }
}

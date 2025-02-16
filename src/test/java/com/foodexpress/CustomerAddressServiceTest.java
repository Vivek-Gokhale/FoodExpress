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

import com.foodexpress.customer.dao.CustomerAddressDao;
import com.foodexpress.customer.model.CustomerAddress;
import com.foodexpress.customer.service.CustomerAddressService;

class CustomerAddressServiceTest {

    @InjectMocks
    private CustomerAddressService customerAddressService;

    @Mock
    private CustomerAddressDao customerAddressDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAddresses() {
        // Mock Data
        CustomerAddress address1 = new CustomerAddress();
        address1.setAid(1);
        address1.setUserId(101);

        CustomerAddress address2 = new CustomerAddress();
        address2.setAid(2);
        address2.setUserId(101);

        List<CustomerAddress> mockAddresses = Arrays.asList(address1, address2);

        // Mock behavior
        when(customerAddressDao.findAllByUserId(101)).thenReturn(mockAddresses);

        // Call the method
        List<CustomerAddress> result = customerAddressService.getAddresses(101);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerAddressDao, times(1)).findAllByUserId(101);
    }

    @Test
    void testUpdateAddress() {
        // Mock Data
        CustomerAddress address = new CustomerAddress();
        address.setAid(1);
        address.setUserId(101);

        // Mock behavior
        when(customerAddressDao.save(address)).thenReturn(address);

        // Call the method
        boolean result = customerAddressService.updateAddress(address);

        // Assertions
        assertTrue(result);
        verify(customerAddressDao, times(1)).save(address);
    }

    @Test
    void testRemoveAddress_WhenExists() {
        // Mock behavior
        when(customerAddressDao.existsById(1)).thenReturn(true);
        doNothing().when(customerAddressDao).deleteById(1);

        // Call the method
        boolean result = customerAddressService.removeAddress(1);

        // Assertions
        assertTrue(result);
        verify(customerAddressDao, times(1)).existsById(1);
        verify(customerAddressDao, times(1)).deleteById(1);
    }

    @Test
    void testRemoveAddress_WhenNotExists() {
        // Mock behavior
        when(customerAddressDao.existsById(1)).thenReturn(false);

        // Call the method
        boolean result = customerAddressService.removeAddress(1);

        // Assertions
        assertFalse(result);
        verify(customerAddressDao, times(1)).existsById(1);
        verify(customerAddressDao, never()).deleteById(1);
    }

    @Test
    void testSetDefaultAddress_Success() {
        // Mock Data
        CustomerAddress address1 = new CustomerAddress();
        address1.setAid(1);
        address1.setUserId(101);
        address1.setDefault(false);

        CustomerAddress address2 = new CustomerAddress();
        address2.setAid(2);
        address2.setUserId(101);
        address2.setDefault(true);

        List<CustomerAddress> userAddresses = Arrays.asList(address1, address2);

        // Mock behavior
        when(customerAddressDao.findById(1)).thenReturn(Optional.of(address1));
        when(customerAddressDao.findAllByUserId(101)).thenReturn(userAddresses);
        when(customerAddressDao.saveAll(userAddresses)).thenReturn(userAddresses);

        // Call the method
        boolean result = customerAddressService.setDefaultAddress(101, 1);

        // Assertions
        assertTrue(result);
        assertTrue(address1.isDefault());
        assertFalse(address2.isDefault());
        verify(customerAddressDao, times(1)).findById(1);
        verify(customerAddressDao, times(1)).findAllByUserId(101);
        verify(customerAddressDao, times(1)).saveAll(userAddresses);
    }

    @Test
    void testSetDefaultAddress_Failure() {
        // Mock behavior
        when(customerAddressDao.findById(1)).thenReturn(Optional.empty());

        // Call the method
        boolean result = customerAddressService.setDefaultAddress(101, 1);

        // Assertions
        assertFalse(result);
        verify(customerAddressDao, times(1)).findById(1);
        verify(customerAddressDao, never()).findAllByUserId(anyInt());
        verify(customerAddressDao, never()).saveAll(anyList());
    }
}

package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.customer.dao.AccountPreferenceDao;
import com.foodexpress.customer.dao.CustomerDao;
import com.foodexpress.customer.model.AccountPreference;
import com.foodexpress.customer.model.Customer;
import com.foodexpress.customer.service.CustomerService;
import com.foodexpress.utilities.PasswordUtils;

class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerDao customerDao;

    @Mock
    private AccountPreferenceDao accountPreferenceDao;

    @Mock
    private PasswordUtils passwordUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterCustomer_Success() {
        // Mock customer data
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("plainPassword");

        // Mock the DAO layer behavior
        when(customerDao.findByEmail("test@example.com")).thenReturn(Optional.empty()); // No existing customer
        when(passwordUtils.hashPassword("plainPassword")).thenReturn("hashedPassword");
        when(customerDao.save(customer)).thenReturn(customer);
        when(accountPreferenceDao.save(any(AccountPreference.class))).thenReturn(new AccountPreference());

        // Call the register method
        boolean result = customerService.registerCustomer(customer);

        // Assertions
        assertTrue(result);
        verify(customerDao, times(1)).save(customer);
        verify(accountPreferenceDao, times(1)).save(any(AccountPreference.class));
    }

    @Test
    void testRegisterCustomer_Failure_DuplicateEmail() {
        // Mock existing customer
        Customer customer = new Customer();
        customer.setEmail("test@example.com");

        when(customerDao.findByEmail("test@example.com")).thenReturn(Optional.of(customer));

        // Call the register method
        boolean result = customerService.registerCustomer(customer);

        // Assertions
        assertFalse(result);
        verify(customerDao, never()).save(customer);  // No save call should happen
    }

    @Test
    void testUpdatePassword_Success() {
        // Mock customer data
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("oldPassword");

        // Mock DAO behavior
        when(customerDao.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(passwordUtils.hashPassword("newPassword")).thenReturn("hashedNewPassword");
        when(customerDao.save(customer)).thenReturn(customer);

        // Call the method
        boolean result = customerService.updatePassword("test@example.com", "newPassword");

        // Assertions
        assertTrue(result);
        assertEquals("hashedNewPassword", customer.getPassword()); // Password should be updated
        verify(customerDao, times(1)).save(customer);
    }

    @Test
    void testUpdatePassword_Failure_CustomerNotFound() {
        // Mock DAO behavior (no customer found)
        when(customerDao.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Call the method
        boolean result = customerService.updatePassword("test@example.com", "newPassword");

        // Assertions
        assertFalse(result);  // No customer found
    }

    @Test
    void testRemoveCustomer_Success() {
        // Mock customer data
        Customer customer = new Customer();
        customer.setUserId(1);

        // Mock DAO behavior
        when(customerDao.findById(1)).thenReturn(Optional.of(customer));

        // Call the method
        boolean result = customerService.removeCustomer(1);

        // Assertions
        assertTrue(result);
        verify(customerDao, times(1)).deleteById(1);
    }

    @Test
    void testRemoveCustomer_Failure_CustomerNotFound() {
        // Mock DAO behavior (no customer found)
        when(customerDao.findById(1)).thenReturn(Optional.empty());

        // Call the method
        boolean result = customerService.removeCustomer(1);

        // Assertions
        assertFalse(result);  // No customer found
        verify(customerDao, never()).deleteById(1);  // No delete should happen
    }

    @Test
    void testIsExist_Success() {
        // Mock customer data
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("hashedPassword");

        // Mock DAO behavior
        when(passwordUtils.hashPassword("password")).thenReturn("hashedPassword");
        when(customerDao.findByEmailAndPassword("test@example.com", "hashedPassword")).thenReturn(Optional.of(customer));

        // Call the method
        boolean result = customerService.isExist("test@example.com", "password");

        // Assertions
        assertTrue(result);
    }

    @Test
    void testIsExist_Failure() {
        // Mock DAO behavior (no customer found)
        when(passwordUtils.hashPassword("password")).thenReturn("hashedPassword");
        when(customerDao.findByEmailAndPassword("test@example.com", "hashedPassword")).thenReturn(Optional.empty());

        // Call the method
        boolean result = customerService.isExist("test@example.com", "password");

        // Assertions
        assertFalse(result);
    }
}

package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.customer.dao.CustomerOrderItemDao;
import com.foodexpress.customer.model.CustomerOrderItem;
import com.foodexpress.customer.service.CustomerOrderItemService;

class CustomerOrderItemServiceTest {

    @InjectMocks
    private CustomerOrderItemService customerOrderItemService;

    @Mock
    private CustomerOrderItemDao customerOrderItemDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderItems() {
        // Mock Data
        CustomerOrderItem item1 = new CustomerOrderItem();
        item1.setItemId(1);
        item1.setId(101);
        
        CustomerOrderItem item2 = new CustomerOrderItem();
        item2.setItemId(2);
        item2.setId(101);

        List<CustomerOrderItem> mockOrderItems = Arrays.asList(item1, item2);

        // Mock behavior
        when(customerOrderItemDao.findOrderItemsByUserIdAndStatus(101)).thenReturn(mockOrderItems);

        // Call the method
        List<CustomerOrderItem> result = customerOrderItemService.getOrderItems(101);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerOrderItemDao, times(1)).findOrderItemsByUserIdAndStatus(101);
    }

    @Test
    void testUpdateCustomerOrderItems() {
        // Mock Data
        CustomerOrderItem item1 = new CustomerOrderItem();
        item1.setItemId(1);
        item1.setId(101);
        
        CustomerOrderItem item2 = new CustomerOrderItem();
        item2.setItemId(2);
        item2.setId(101);

        List<CustomerOrderItem> mockOrderItems = Arrays.asList(item1, item2);

        // Mock behavior
        when(customerOrderItemDao.saveAll(mockOrderItems)).thenReturn(mockOrderItems);

        // Call the method
        List<CustomerOrderItem> result = customerOrderItemService.updateCustomerOrderItems(mockOrderItems);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerOrderItemDao, times(1)).saveAll(mockOrderItems);
    }
}

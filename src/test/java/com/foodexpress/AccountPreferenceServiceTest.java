package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.customer.dao.AccountPreferenceDao;
import com.foodexpress.customer.model.AccountPreference;
import com.foodexpress.customer.service.AccountPreferenceService;

class AccountPreferenceServiceTest {

    @InjectMocks
    private AccountPreferenceService accountPreferenceService;

    @Mock
    private AccountPreferenceDao accountPreferenceDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdatePreference_Success() {
        // Mock data
        AccountPreference preference = new AccountPreference();
        preference.setUserId(1);

        // Mock behavior
        when(accountPreferenceDao.save(preference)).thenReturn(preference);

        // Call the method
        boolean result = accountPreferenceService.updatePreference(preference);

        // Assertions
        assertTrue(result);
        verify(accountPreferenceDao, times(1)).save(preference);
    }

    @Test
    void testUpdatePreference_Failure() {
        // Mock behavior
        when(accountPreferenceDao.save(any())).thenReturn(null);

        // Call the method
        boolean result = accountPreferenceService.updatePreference(new AccountPreference());

        // Assertions
        assertFalse(result);
        verify(accountPreferenceDao, times(1)).save(any());
    }

    @Test
    void testGetAccountPreference_Success() {
        // Mock data
        AccountPreference preference = new AccountPreference();
        preference.setUserId(1);

        // Mock behavior
        when(accountPreferenceDao.findByUserId(1)).thenReturn(Optional.of(preference));

        // Call the method
        AccountPreference result = accountPreferenceService.getAccountPreference(1);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getUserId());
        verify(accountPreferenceDao, times(1)).findByUserId(1);
    }

    @Test
    void testGetAccountPreference_NotFound() {
        // Mock behavior
        when(accountPreferenceDao.findByUserId(1)).thenReturn(Optional.empty());

        // Expect an exception when calling the method
        assertThrows(NoSuchElementException.class, () -> accountPreferenceService.getAccountPreference(1));

        // Verify interaction
        verify(accountPreferenceDao, times(1)).findByUserId(1);
    }
}

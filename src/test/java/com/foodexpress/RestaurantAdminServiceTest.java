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

import com.foodexpress.admin.dao.RestaurantAdminDao;
import com.foodexpress.admin.dao.RestaurantRegisterDao;
import com.foodexpress.admin.dto.DashboardDTO;
import com.foodexpress.admin.model.RestaurantAdmin;
import com.foodexpress.admin.service.RestaurantAdminService;
import com.foodexpress.utilities.PasswordUtils;

class RestaurantAdminServiceTest {

    @InjectMocks
    private RestaurantAdminService adminService;

    @Mock
    private RestaurantAdminDao adminDao;

    @Mock
    private RestaurantRegisterDao registerDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAdmin() {
        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setPassword("password123");

        when(adminDao.save(any(RestaurantAdmin.class))).thenReturn(admin);

        boolean result = adminService.addAdmin(admin);

        assertTrue(result);
        verify(adminDao, times(1)).save(any(RestaurantAdmin.class));
    }

    @Test
    void testUpdateAdmin_Success() {
        RestaurantAdmin existingAdmin = new RestaurantAdmin();
        existingAdmin.setAdminId(1);
        existingAdmin.setEmail("old@example.com");

        RestaurantAdmin updatedAdmin = new RestaurantAdmin();
        updatedAdmin.setAdminId(1);
        updatedAdmin.setEmail("new@example.com");
        updatedAdmin.setPassword("newPassword");

        when(adminDao.existsById(1)).thenReturn(true);
        when(adminDao.findById(1)).thenReturn(Optional.of(existingAdmin));
        when(adminDao.save(any(RestaurantAdmin.class))).thenReturn(updatedAdmin);

        boolean result = adminService.updateAdmin(updatedAdmin);

        assertTrue(result);
        verify(adminDao, times(1)).existsById(1);
        verify(adminDao, times(1)).save(any(RestaurantAdmin.class));
    }

    @Test
    void testUpdateAdmin_Failure() {
        RestaurantAdmin updatedAdmin = new RestaurantAdmin();
        updatedAdmin.setAdminId(1);

        when(adminDao.existsById(1)).thenReturn(false);

        boolean result = adminService.updateAdmin(updatedAdmin);

        assertFalse(result);
        verify(adminDao, times(1)).existsById(1);
        verify(adminDao, never()).save(any(RestaurantAdmin.class));
    }

    @Test
    void testDeleteAdmin_Success() {
        when(adminDao.existsById(1)).thenReturn(true);

        boolean result = adminService.deleteAdmin(1);

        assertTrue(result);
        verify(adminDao, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAdmin_Failure() {
        when(adminDao.existsById(1)).thenReturn(false);

        boolean result = adminService.deleteAdmin(1);

        assertFalse(result);
        verify(adminDao, never()).deleteById(anyInt());
    }

    @Test
    void testGetAdmins() {
        RestaurantAdmin admin1 = new RestaurantAdmin();
        RestaurantAdmin admin2 = new RestaurantAdmin();

        when(adminDao.findAllByRestaurantId(101)).thenReturn(Arrays.asList(admin1, admin2));

        List<RestaurantAdmin> admins = adminService.getAdmins(101);

        assertNotNull(admins);
        assertEquals(2, admins.size());
        verify(adminDao, times(1)).findAllByRestaurantId(101);
    }

    @Test
    void testAuthenticateAdmin_Success() {
        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setUsername("admin1");

        when(adminDao.findByRestaurantIdAndEmailAndPassword(anyInt(), anyString(), anyString()))
                .thenReturn(Optional.of(admin));

        String username = adminService.authenticateAdmin(101, "admin@example.com", "password123");

        assertEquals("admin1", username);
        verify(adminDao, times(1)).findByRestaurantIdAndEmailAndPassword(anyInt(), anyString(), anyString());
    }

    @Test
    void testGetDashBoardDTO() {
        when(adminDao.findTotalPriceByRestaurantId(101)).thenReturn(1000);
        when(adminDao.countDistinctUsersByRestaurantId(101)).thenReturn(50);
        when(adminDao.findAvgRatingWithCeil(101)).thenReturn(4);
        when(adminDao.countItemsByRestaurantId(101)).thenReturn(20);
        when(adminDao.findRestaurantNameById(101)).thenReturn("Test Restaurant");
        when(adminDao.findRestaurantLocationById(101)).thenReturn("Test Location");

        DashboardDTO dashboardDTO = adminService.getDashBoardDTO(101);

        assertNotNull(dashboardDTO);
        assertEquals(1000, dashboardDTO.getTotalPrice());
        assertEquals(50, dashboardDTO.getUserCount());
        assertEquals(4, dashboardDTO.getAvgRating());
        assertEquals(20, dashboardDTO.getMenuItems());
        assertEquals("Test Restaurant", dashboardDTO.getRestaurantName());
        assertEquals("Test Location", dashboardDTO.getLocation());
        verify(adminDao, times(1)).findTotalPriceByRestaurantId(101);
        verify(adminDao, times(1)).countDistinctUsersByRestaurantId(101);
    }
}


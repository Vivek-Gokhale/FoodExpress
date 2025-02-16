package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.admin.dao.RestaurantOrderDao;
import com.foodexpress.admin.dto.CustomerOrderStats;
import com.foodexpress.admin.dto.OrderPerformance;
import com.foodexpress.admin.dto.OrderPerformanceMonthly;
import com.foodexpress.admin.dto.OrderPerformanceWeekly;
import com.foodexpress.admin.service.RestaurantOrderService;

class RestaurantOrderServiceTest {

    @Mock
    private RestaurantOrderDao restaurantOrderDao;

    @InjectMocks
    private RestaurantOrderService restaurantOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCustomerOrderStats() {
        int restaurantId = 1;
        LocalDate date7 = LocalDate.now().minusDays(7);
        LocalDate date30 = LocalDate.now().minusDays(30);
        LocalDate date365 = LocalDate.now().minusDays(365);

        CustomerOrderStats mockStats = new CustomerOrderStats();
        mockStats.setOrdersInLast7Days(5);
        mockStats.setOrdersInLast30Days(20);
        mockStats.setOrdersInLast365Days(100);

        when(restaurantOrderDao.findCustomerOrderStats(restaurantId, date7, date30, date365))
                .thenReturn(Collections.singletonList(mockStats));

        List<CustomerOrderStats> stats = restaurantOrderService.getCustomerOrderStats(restaurantId, date7, date30, date365);

        assertNotNull(stats);
        assertEquals(1, stats.size());
        assertEquals(125, stats.get(0).getTotalOrders());
        assertEquals(5.0, stats.get(0).getAvgOrdersPerWeek());
        assertEquals(20.0, stats.get(0).getAvgOrdersPerMonth());
        assertEquals(100.0, stats.get(0).getAvgOrdersPerYear());
    }

    @Test
    void testGetMonthlyReport() {
        int restaurantId = 1;

        Object[] mockData = {"January", 2025, 500};
        when(restaurantOrderDao.getMonthlyReportForRestaurant(restaurantId))
                .thenReturn(Collections.singletonList(mockData));

        List<OrderPerformanceMonthly> report = restaurantOrderService.getMonthlyReport(restaurantId);

        assertNotNull(report);
        assertEquals(1, report.size());
        assertEquals("January", report.get(0).getMonth());
        assertEquals(2025, report.get(0).getYear());
        assertEquals(500, report.get(0).getTotalOrders());
    }

    @Test
    void testGetWeeklyReport() {
        int restaurantId = 1;

        Object[] mockData = {"Week 1", 2025, 150};
        when(restaurantOrderDao.getWeeklyReportForRestaurant(restaurantId))
                .thenReturn(Collections.singletonList(mockData));

        List<OrderPerformanceWeekly> report = restaurantOrderService.getWeeklyReport(restaurantId);

        assertNotNull(report);
        assertEquals(1, report.size());
        assertEquals("Week 1", report.get(0).getWeekNumber());
        assertEquals(2025, report.get(0).getYear());
        assertEquals(150, report.get(0).getTotalOrders());
    }

    @Test
    void testGetDailyReport() {
        int restaurantId = 1;

        Object[] mockData = {LocalDate.now(), 50};
        when(restaurantOrderDao.getDailyReportForRestaurant(restaurantId))
                .thenReturn(Collections.singletonList(mockData));

        List<OrderPerformance> report = restaurantOrderService.getDailyReport(restaurantId);

        assertNotNull(report);
        assertEquals(1, report.size());
        assertEquals(LocalDate.now(), report.get(0).getReportDate());
        assertEquals(50, report.get(0).getTotalOrders());
    }
}

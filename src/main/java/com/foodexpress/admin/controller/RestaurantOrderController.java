package com.foodexpress.admin.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.admin.dto.CustomerOrderStats;
import com.foodexpress.admin.dto.OrderPerformance;
import com.foodexpress.admin.dto.OrderPerformanceMonthly;
import com.foodexpress.admin.dto.OrderPerformanceWeekly;
import com.foodexpress.admin.service.RestaurantOrderService;

@RestController
public class RestaurantOrderController {

    @Autowired
    private RestaurantOrderService restaurantOrderService;

    @GetMapping("customer-stats/{restaurantId}")
    public List<CustomerOrderStats> getCustomerOrderStats(
            @PathVariable("restaurantId") int restaurantId) {

    	
        LocalDate today = LocalDate.now();
        LocalDate date7 = today.minusDays(7);    // 7 days ago
        LocalDate date30 = today.minusDays(30);  // 30 days ago
        LocalDate date365 = today.minusDays(365); // 365 days ago
        
        // Pass the calculated dates to the service layer
        return restaurantOrderService.getCustomerOrderStats(restaurantId, date7, date30, date365);
    }
    
    @GetMapping("get-daily-report/{restaurantId}")
    public List<OrderPerformance> getDailyReportHandlder(@PathVariable Integer restaurantId) {
        return restaurantOrderService.getDailyReport(restaurantId);
    }

    @GetMapping("get-weekly-report/{restaurantId}")
    public List<OrderPerformanceWeekly> getWeeklyReportHandlder(@PathVariable Integer restaurantId) {
        return restaurantOrderService.getWeeklyReport(restaurantId);
    }

    @GetMapping("get-monthly-report/{restaurantId}")
    public List<OrderPerformanceMonthly> getMonthlyReportHandlder(@PathVariable Integer restaurantId) {
        return restaurantOrderService.getMonthlyReport(restaurantId);
    }
   
}

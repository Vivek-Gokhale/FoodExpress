package com.foodexpress.admin.service;

import java.time.LocalDate;
import java.util.List;

import com.foodexpress.admin.dto.CustomerOrderStats;
import com.foodexpress.admin.dto.OrderPerformance;
import com.foodexpress.admin.dto.OrderPerformanceMonthly;
import com.foodexpress.admin.dto.OrderPerformanceWeekly;

public interface IRestaurantOrder {


	List<CustomerOrderStats> getCustomerOrderStats(int restaurantId, LocalDate date7, LocalDate date30,
			LocalDate date365);
	
	 public List<OrderPerformanceMonthly> getMonthlyReport(Integer restaurantId);
	 public List<OrderPerformanceWeekly> getWeeklyReport(Integer restaurantId);
	 public List<OrderPerformance> getDailyReport(Integer restaurantId);
	
}

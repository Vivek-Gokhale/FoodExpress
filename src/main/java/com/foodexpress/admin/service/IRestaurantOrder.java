package com.foodexpress.admin.service;

import java.time.LocalDate;
import java.util.List;

import com.foodexpress.admin.dto.CustomerOrderStats;
import com.foodexpress.admin.dto.OrderPerformance;

public interface IRestaurantOrder {


	List<CustomerOrderStats> getCustomerOrderStats(int restaurantId, LocalDate date7, LocalDate date30,
			LocalDate date365);
	
	 public List<OrderPerformance> getMonthlyReport(Integer restaurantId);
	 public List<OrderPerformance> getWeeklyReport(Integer restaurantId);
	 public List<OrderPerformance> getDailyReport(Integer restaurantId);
	
}

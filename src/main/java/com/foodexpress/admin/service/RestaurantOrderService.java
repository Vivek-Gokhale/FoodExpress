package com.foodexpress.admin.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.RestaurantOrderDao;
import com.foodexpress.admin.dto.CustomerOrderStats;
import com.foodexpress.admin.dto.OrderPerformance;
import com.foodexpress.admin.dto.OrderPerformanceMonthly;
import com.foodexpress.admin.dto.OrderPerformanceWeekly;
import com.foodexpress.utilities.DateUtils;

@Service
public class RestaurantOrderService implements IRestaurantOrder {

    @Autowired
    private RestaurantOrderDao restaurantOrderDao;

   
    
  


	@Override
	public List<CustomerOrderStats> getCustomerOrderStats(int restaurantId, LocalDate date7, LocalDate date30,
			LocalDate date365) {
		List<CustomerOrderStats> stats = restaurantOrderDao.findCustomerOrderStats(restaurantId, date7, date30, date365);

        // For each customer stats, calculate the total orders and averages
        for (CustomerOrderStats stat : stats) {
            // Calculate the total orders in the given time periods
            stat.setTotalOrders(stat.getOrdersInLast7Days() + stat.getOrdersInLast30Days() + stat.getOrdersInLast365Days());

          
            stat.setAvgOrdersPerWeek(stat.getOrdersInLast7Days() / 1.0); // assuming 1 week for 7 days
            stat.setAvgOrdersPerMonth(stat.getOrdersInLast30Days() / 1.0); // assuming 1 month for 30 days
            stat.setAvgOrdersPerYear(stat.getOrdersInLast365Days() / 1.0); // assuming 1 year for 365 days
        }

        return stats;
	}






	@Override
	public List<OrderPerformanceMonthly> getMonthlyReport(Integer restaurantId) {
		// TODO Auto-generated method stub
		return OrderPerformanceMonthly.fromObjectArrayList(restaurantOrderDao.getMonthlyReportForRestaurant(restaurantId));
	}






	@Override
	public List<OrderPerformanceWeekly> getWeeklyReport(Integer restaurantId) {
		// TODO Auto-generated method stub
		return OrderPerformanceWeekly.fromObjectArrayList(restaurantOrderDao.getWeeklyReportForRestaurant(restaurantId));
	}






	@Override
	public List<OrderPerformance> getDailyReport(Integer restaurantId) {
		// TODO Auto-generated method stub
		return OrderPerformance.fromObjectArrayList(restaurantOrderDao.getDailyReportForRestaurant(restaurantId));
	}

	

	
}

package com.foodexpress.admin.service;

import java.util.List;

import com.foodexpress.admin.dto.CustomerMenuPreferenceStats;
import com.foodexpress.admin.model.OrderItem;

public interface IOrderItem {
	public List<OrderItem> getOrderItems(int restaurantId);
	public boolean updateOrderStatus(OrderItem orderItem);
	public List<CustomerMenuPreferenceStats> getMenuPreferencesForRestaurant(int restaurantId);
}

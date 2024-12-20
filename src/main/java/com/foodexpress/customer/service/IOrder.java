package com.foodexpress.customer.service;

import java.util.List;

import com.foodexpress.customer.model.Order;

public interface IOrder {
	public List<Order> getOrders(int userId);
}

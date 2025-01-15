package com.foodexpress.customer.service;

import java.util.List;

import com.foodexpress.admin.model.OrderItem;
import com.foodexpress.customer.model.CustomerOrderItem;

public interface ICustomerOrderItem {
	public List<CustomerOrderItem> getOrderItems(Integer userId);
	public List<CustomerOrderItem> updateCustomerOrderItems(List<CustomerOrderItem> items);
}

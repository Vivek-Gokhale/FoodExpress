package com.foodexpress.customer.service;

import java.util.List;
import java.util.Map;

import com.foodexpress.customer.model.Customer;
import com.foodexpress.customer.model.Order;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;

public interface IOrder {
	public List<Order> getOrders(int userId);
	
	Order placeOrder(Integer userId, Integer paymentFlag, double finalAmount, Map<Integer, String> itemRequestMap);

	public List<DeliveryPartner> getDeliveryStatus(Integer orderId);
	
	public Customer getCustomer(Integer userId);
}

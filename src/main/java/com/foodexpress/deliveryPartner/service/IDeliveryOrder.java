package com.foodexpress.deliveryPartner.service;

import java.util.List;

import com.foodexpress.customer.model.Customer;
import com.foodexpress.deliveryPartner.dto.OrderStatus;

public interface IDeliveryOrder {
	public boolean selectDeliveryOrder(Integer partnerId, Integer orderId, String longitude, String latitude);
	public boolean verifyOTP(Integer deliveryId, Integer userId);
	public List<OrderStatus> getPendingOrders(Integer partnerId);
	public List<OrderStatus> getCompletedOrders(Integer artnerId);
	public Customer getCustomer(Integer userId);
	public Customer getCustomerByOrderId(Integer orderId);
}

package com.foodexpress.customer.service;

import com.foodexpress.customer.dto.OrderSummary;

public interface IAppliedCoupon {
	public OrderSummary getOrderSummary(Integer userId);
	int applyCoupon(String couponCode, int userId);
}

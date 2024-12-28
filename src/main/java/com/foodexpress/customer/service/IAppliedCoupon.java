package com.foodexpress.customer.service;

import com.foodexpress.customer.dto.OrderSummary;

public interface IAppliedCoupon {
	public int applyCoupon(int couponId, int userId);
	public OrderSummary getOrderSummary(Integer userId);
}

package com.foodexpress.customer.service;

import java.util.List;

import com.foodexpress.customer.model.Coupon;

public interface ICoupon {
	
	public List<Coupon> getValidCoupons(int userId);

}

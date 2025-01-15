package com.foodexpress.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.customer.model.Coupon;
import com.foodexpress.customer.service.CouponService;

@RestController
public class CouponController {

	
	@Autowired
	private CouponService couponService;
	@GetMapping("get-valid-coupon/{userId}")
	public List<Coupon> getCouponsHandler(@PathVariable("userId") int userId)
	{
		return couponService.getValidCoupons(userId);
	}
	
	
}

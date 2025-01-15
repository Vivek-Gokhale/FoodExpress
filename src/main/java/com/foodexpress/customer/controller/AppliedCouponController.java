package com.foodexpress.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.customer.dto.OrderSummary;
import com.foodexpress.customer.service.AppliedCouponService;

@RestController
public class AppliedCouponController {

	@Autowired
	private AppliedCouponService appliedCouponService;
	
	
	@GetMapping("/verify-coupon/{couponCode}")
	public ResponseEntity<String> verifyCouponHandler(@PathVariable("couponCode") String couponCode) {
	    try {
	        // Call the service method to verify the coupon
	        if(appliedCouponService.verifyCoupon(couponCode));
	        return ResponseEntity.ok("Coupon is valid");
	    } catch (Exception e) {
	        // Return error response if the coupon verification fails
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid coupon");
	    }
	}

	
	@PostMapping("apply-coupon/{userId}/{couponCode}")
	public ResponseEntity<String> applyCouponHandler(@PathVariable("userId") int userId, 
	                                                 @PathVariable("couponCode") String couponCode) {
	    int result = appliedCouponService.applyCoupon(couponCode, userId);

	    switch (result) {
	        case 1:
	            return ResponseEntity.ok("Coupon applied successfully.");
	        case 2:
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Minimum order value not satisfied.");
	        case 3:
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coupon usage count expired.");
	        default:
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
	    }
	}

	
	@GetMapping("get-order_summary/{userId}")
	public OrderSummary applyCouponHandler(@PathVariable("userId") int userId)
	{
		return appliedCouponService.getOrderSummary(userId);	
	}
}

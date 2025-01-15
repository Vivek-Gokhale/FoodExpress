package com.foodexpress.customer.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodexpress.customer.dao.CouponDao;
import com.foodexpress.customer.dao.OrderDao;
import com.foodexpress.customer.model.Coupon;
import com.foodexpress.customer.model.Order;

@Service
public class CouponService implements ICoupon{

	
	@Autowired
	private CouponDao couponDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Override
	public List<Coupon> getValidCoupons(int userId) {
	    
	    List<Coupon> allCoupons = couponDao.findAll();
	    List<Order> allOrders = orderDao.findAllByUserId(userId);

	    Set<Integer> usedCouponIds = new HashSet<>();

	    for (Order order : allOrders) {
	        String couponIdsJson = order.getCouponIds(); // Assuming getCouponIds() returns JSON
	        if (couponIdsJson != null && !couponIdsJson.isEmpty()) {
	            try {
	                // Parse JSON and extract coupon IDs
	                ObjectMapper objectMapper = new ObjectMapper();
	                List<Integer> couponIds = objectMapper.readValue(couponIdsJson, new TypeReference<List<Integer>>() {});
	                usedCouponIds.addAll(couponIds);
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	                // Handle JSON parsing exception
	            }
	        }
	    }

	    List<Coupon> validCoupons = allCoupons.stream()
	        .filter(coupon -> !usedCouponIds.contains(coupon.getCouponId()))
	        .collect(Collectors.toList());

	    return validCoupons;
	}


}

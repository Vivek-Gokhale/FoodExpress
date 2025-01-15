package com.foodexpress.customer.service;

import java.awt.Frame;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.admin.model.RestaurantMenuItem;
import com.foodexpress.customer.dao.AppliedCouponDao;
import com.foodexpress.customer.dao.CartDao;
import com.foodexpress.customer.dao.CouponDao;
import com.foodexpress.customer.dto.OrderSummary;
import com.foodexpress.customer.model.AppliedCoupon;
import com.foodexpress.customer.model.Cart;
import com.foodexpress.customer.model.Coupon;

@Service
public class AppliedCouponService implements IAppliedCoupon{

	@Autowired
	private AppliedCouponDao appliedCouponDao;
	
	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private MenuItemDao menuItemDao;
	
	@Autowired
	private CouponDao couponDao;
	 @Override
	    public int applyCoupon(String couponCode, int userId) {
	        // Create an AppliedCoupon object and store it
		    int couponId = couponDao.findByCode(couponCode).getCouponId();
	        AppliedCoupon appliedCoupon = AppliedCoupon.builder()
	                .userId(userId)
	                .couponIds(couponId)
	                .build();
	        
	        Optional<Coupon> coupon = couponDao.findById(couponId);
	        
	        
	        List<Cart> carts = cartDao.findByUserId(userId);
	        Map<Integer, Double> itemPriceMap = new HashMap<>();

		     for (Cart cart : carts) {
		         Integer itemId = cart.getItemId();
		         Double price = (double) menuItemDao.findById(itemId)
		                 .orElseThrow(() -> new RuntimeException("Item not found"))
		                 .getPrice();
		         itemPriceMap.put(itemId, price * cart.getQuantity());
		     }

		     double totalAmount = itemPriceMap.values().stream().mapToDouble(Double::doubleValue).sum();
		     
		    if(totalAmount < coupon.get().getMinOrderValue())
		    {
		    	return 2; //means minimum order value not satified
		    }
		    else if(coupon.get().getUsageCount() <= 0)
		    {
		    	return 3; //means no of usage count expired
		    }
		    else {
		    	appliedCouponDao.save(appliedCoupon);
		    	return 1;
			}
	        
	       
	    }

	 @Override
	 public OrderSummary getOrderSummary(Integer userId) {
		 
	     List<AppliedCoupon> appliedCoupons = appliedCouponDao.findByUserId(userId);
	     Set<Integer> mySet = new HashSet<>();

	     for (AppliedCoupon appliedCoupon : appliedCoupons) {
	         mySet.add(appliedCoupon.getAppliedCouponsId());
	     }

	     List<Cart> carts = cartDao.findByUserId(userId);
	     List<Coupon> coupons = couponDao.findAll();

	     Map<Integer, Double> itemPriceMap = new HashMap<>();

	     for (Cart cart : carts) {
	         Integer itemId = cart.getItemId();
	         Double price = (double) menuItemDao.findById(itemId)
	                 .orElseThrow(() -> new RuntimeException("Item not found"))
	                 .getPrice();
	         itemPriceMap.put(itemId, price * cart.getQuantity());
	     }

	     double totalAmount = itemPriceMap.values().stream().mapToDouble(Double::doubleValue).sum();

	     double couponDiscount = 0; // Placeholder for coupon discount calculation
	    
	     double couponMaxPrice = 0;
	     for (Coupon coupon : coupons) {
	         if (mySet.contains(coupon.getCouponId())) {
	             couponDiscount += coupon.getPercentage(); // Adjust as per your logic
	             couponMaxPrice += coupon.getMaxDiscountValue();
	         }
	     }
	     double couponDiscountPrice = (totalAmount * couponDiscount) / 100;
	     
	     if (couponDiscountPrice > couponMaxPrice) {
	         couponDiscountPrice = couponMaxPrice;
	     }
	    
	     double tax = totalAmount * 0.1;

	     
	     double savings = couponDiscountPrice; // Placeholder for savings

	     double finalAmount = totalAmount - couponDiscountPrice + tax;

	     return OrderSummary.builder()
	             .totalAmount(totalAmount)
	             .couponDiscount(couponDiscountPrice)
	             .tax(tax)
	             .savings(savings)
	             .finalAmount(finalAmount)
	             .build();
	 }

	public boolean verifyCoupon(String couponCode) {
		// TODO Auto-generated method stub
		 Coupon coupon = couponDao.findByCode(couponCode);
		 System.out.println(coupon);
		 if(coupon != null)
			 return true;
		 return false;
		
	}

}

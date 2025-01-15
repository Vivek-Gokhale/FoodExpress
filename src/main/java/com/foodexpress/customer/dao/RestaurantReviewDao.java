package com.foodexpress.customer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.customer.model.RestaurantReview;

@Repository
public interface RestaurantReviewDao extends JpaRepository<RestaurantReview, Integer>{
	public List<RestaurantReview> findByRestaurantId(Integer restaurantId);
	public RestaurantReview findByUserIdAndRestaurantIdAndOrderId(Integer userId, Integer restaurantId, Integer orderId);
	public RestaurantReview findByUserIdAndRestaurantId(Integer userId, Integer restaurantId);
	
}

package com.foodexpress.customer.service;

import java.util.List;

import com.foodexpress.customer.model.RestaurantReview;

public interface IRestaurantReview {
	public List<RestaurantReview> getRestaurantReviews(int restaurantId);
	public RestaurantReview giveReview(RestaurantReview restaurantReview);
	public RestaurantReview updateReview(RestaurantReview restaurantReview);
	public double getAvgRestaurantRating(int restaurantId);
	public RestaurantReview getRestaurantReviewForEdit(int userId, int restaurantId, int orderId);
}

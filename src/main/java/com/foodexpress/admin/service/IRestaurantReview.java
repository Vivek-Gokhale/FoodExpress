package com.foodexpress.admin.service;

import java.util.List;
import java.util.Optional;

import com.foodexpress.admin.model.RestaurantReview;

public interface IRestaurantReview {
	public List<RestaurantReview> getReviews(int restaurantId);
	public Optional<RestaurantReview> updateResponse(RestaurantReview review);
}

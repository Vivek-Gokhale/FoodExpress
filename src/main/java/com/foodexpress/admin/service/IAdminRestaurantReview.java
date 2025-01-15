package com.foodexpress.admin.service;

import java.util.List;
import java.util.Optional;

import com.foodexpress.admin.model.AdminRestaurantReview;

public interface IAdminRestaurantReview {
	public List<AdminRestaurantReview> getReviews(int restaurantId);
	public Optional<AdminRestaurantReview> updateResponse(AdminRestaurantReview review);
}

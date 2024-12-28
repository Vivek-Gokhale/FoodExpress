package com.foodexpress.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.AdminRestaurantReviewDao;
import com.foodexpress.admin.model.AdminRestaurantReview;


@Service
public class AdminRestaurantReviewService implements IAdminRestaurantReview{

	@Autowired
	AdminRestaurantReviewDao restaurantReviewDao;
	@Override
	public List<AdminRestaurantReview> getReviews(int restaurantId) {
		return restaurantReviewDao.findByRestaurantId(restaurantId);
	}


	@Override
	public Optional<AdminRestaurantReview> updateResponse(AdminRestaurantReview review) {
	    if (review != null && review.getReviewId() != null) {
	        AdminRestaurantReview updatedReview = restaurantReviewDao.save(review);
	        return Optional.of(updatedReview);
	    }
	    return Optional.empty();
	}

}

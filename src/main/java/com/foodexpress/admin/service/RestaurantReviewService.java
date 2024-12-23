package com.foodexpress.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.RestaurantReviewDao;
import com.foodexpress.admin.model.RestaurantReview;


@Service
public class RestaurantReviewService implements IRestaurantReview{

	@Autowired
	RestaurantReviewDao restaurantReviewDao;
	@Override
	public List<RestaurantReview> getReviews(int restaurantId) {
		return restaurantReviewDao.findByRestaurantId(restaurantId);
	}


	@Override
	public Optional<RestaurantReview> updateResponse(RestaurantReview review) {
	    if (review != null && review.getReviewId() != null) {
	        RestaurantReview updatedReview = restaurantReviewDao.save(review);
	        return Optional.of(updatedReview);
	    }
	    return Optional.empty();
	}

}

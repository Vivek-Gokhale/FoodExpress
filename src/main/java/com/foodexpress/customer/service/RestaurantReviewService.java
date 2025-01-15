package com.foodexpress.customer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.customer.dao.CustomerDao;
import com.foodexpress.customer.dao.RestaurantReviewDao;
import com.foodexpress.customer.model.RestaurantReview;

@Service
public class RestaurantReviewService implements IRestaurantReview {

    @Autowired
    private RestaurantReviewDao restaurantReviewDao;
    
    @Autowired
    private CustomerDao customerDao;

    @Override
    public List<RestaurantReview> getRestaurantReviews(int restaurantId) {
        // Fetch all reviews for a specific restaurant by its ID
        List<RestaurantReview> reviews =  restaurantReviewDao.findByRestaurantId(restaurantId);
        for(RestaurantReview review : reviews)
        {
        	String firstName = customerDao.findById(review.getUserId()).get().getFirstName();
        	String lastName = customerDao.findById(review.getUserId()).get().getLastName();
        	review.setUsernameString(firstName + " " + lastName);
        }
        return reviews;
    }

    @Override
    public RestaurantReview giveReview(RestaurantReview restaurantReview) {
        // Save a new review for a restaurant
        return restaurantReviewDao.save(restaurantReview);
    }

    @Override
    public RestaurantReview updateReview(RestaurantReview restaurantReview) {
        // Update an existing review if it exists
        return restaurantReviewDao.save(restaurantReview);
    }

    @Override
    public double getAvgRestaurantRating(int restaurantId) {
        // Fetch all reviews for the given restaurant ID
        List<RestaurantReview> reviews = restaurantReviewDao.findByRestaurantId(restaurantId);

        // Check if there are no reviews to avoid division by zero
        if (reviews.isEmpty()) {
            return 0.0;
        }

        // Calculate the total sum of ratings
        int totalRating = 0;
        for (RestaurantReview review : reviews) {
            totalRating += review.getRating();
        }

        // Calculate and return the average as a double
        return (double) totalRating / reviews.size();
    }

	@Override
	public RestaurantReview getRestaurantReviewForEdit(int userId, int restaurantId, int orderId) {
		// TODO Auto-generated method stub
		return restaurantReviewDao.findByUserIdAndRestaurantIdAndOrderId(userId, restaurantId, orderId);
	}

	public boolean checkReviewExists(int userId, int restaurantId) {
		// TODO Auto-generated method stub
		RestaurantReview review = restaurantReviewDao.findByUserIdAndRestaurantId(userId, restaurantId);
		if(review != null)
			return true;
		return false;
		
	}

}

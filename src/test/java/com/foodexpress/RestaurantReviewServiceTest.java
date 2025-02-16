package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.customer.dao.RestaurantReviewDao;
import com.foodexpress.customer.model.RestaurantReview;

class RestaurantReviewServiceTest {

    @InjectMocks
    private RestaurantReviewService restaurantReviewService;

    @Mock
    private RestaurantReviewDao restaurantReviewDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRestaurantReviews() {
        // Mock data
        int restaurantId = 1;
        List<RestaurantReview> reviews = new ArrayList<>();
        RestaurantReview review = new RestaurantReview();
        review.setRating(4);
        reviews.add(review);

        // Mock DAO behavior
        when(restaurantReviewDao.findByRestaurantId(restaurantId)).thenReturn(reviews);

        // Call the method
        List<RestaurantReview> result = restaurantReviewService.getRestaurantReviews(restaurantId);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getRating());
        verify(restaurantReviewDao, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    void testGiveReview() {
        // Mock data
        RestaurantReview review = new RestaurantReview();
        review.setRestaurantId(1);
        review.setUserId(1);
        review.setRating(5);

        // Mock DAO behavior
        when(restaurantReviewDao.save(review)).thenReturn(review);

        // Call the method
        RestaurantReview result = restaurantReviewService.giveReview(review);

        // Assertions
        assertNotNull(result);
        assertEquals(5, result.getRating());
        verify(restaurantReviewDao, times(1)).save(review);
    }

    @Test
    void testUpdateReview() {
        // Mock data
        RestaurantReview review = new RestaurantReview();
        review.setRestaurantId(1);
        review.setUserId(1);
        review.setRating(4);

        // Mock DAO behavior
        when(restaurantReviewDao.save(review)).thenReturn(review);

        // Call the method
        RestaurantReview result = restaurantReviewService.updateReview(review);

        // Assertions
        assertNotNull(result);
        assertEquals(4, result.getRating());
        verify(restaurantReviewDao, times(1)).save(review);
    }

    @Test
    void testGetAvgRestaurantRating() {
        // Mock data
        int restaurantId = 1;
        List<RestaurantReview> reviews = new ArrayList<>();
        RestaurantReview review1 = new RestaurantReview();
        review1.setRating(4);
        RestaurantReview review2 = new RestaurantReview();
        review2.setRating(5);
        reviews.add(review1);
        reviews.add(review2);

        // Mock DAO behavior
        when(restaurantReviewDao.findByRestaurantId(restaurantId)).thenReturn(reviews);

        // Call the method
        double result = restaurantReviewService.getAvgRestaurantRating(restaurantId);

        // Assertions
        assertEquals(4.5, result);
        verify(restaurantReviewDao, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    void testGetAvgRestaurantRating_NoReviews() {
        // Mock data
        int restaurantId = 1;
        List<RestaurantReview> reviews = new ArrayList<>();

        // Mock DAO behavior
        when(restaurantReviewDao.findByRestaurantId(restaurantId)).thenReturn(reviews);

        // Call the method
        double result = restaurantReviewService.getAvgRestaurantRating(restaurantId);

        // Assertions
        assertEquals(0.0, result);
        verify(restaurantReviewDao, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    void testGetRestaurantReviewForEdit() {
        // Mock data
        int userId = 1;
        int restaurantId = 1;
        int orderId = 1;
        RestaurantReview review = new RestaurantReview();
        review.setUserId(userId);
        review.setRestaurantId(restaurantId);
        review.setOrderId(orderId);

        // Mock DAO behavior
        when(restaurantReviewDao.findByUserIdAndRestaurantIdAndOrderId(userId, restaurantId, orderId)).thenReturn(review);

        // Call the method
        RestaurantReview result = restaurantReviewService.getRestaurantReviewForEdit(userId, restaurantId, orderId);

        // Assertions
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(restaurantId, result.getRestaurantId());
        assertEquals(orderId, result.getOrderId());
        verify(restaurantReviewDao, times(1)).findByUserIdAndRestaurantIdAndOrderId(userId, restaurantId, orderId);
    }
}

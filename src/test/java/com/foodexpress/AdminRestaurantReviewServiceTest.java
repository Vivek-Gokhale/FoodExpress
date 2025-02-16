package com.foodexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.admin.dao.AdminRestaurantReviewDao;
import com.foodexpress.admin.model.AdminRestaurantReview;
import com.foodexpress.admin.service.AdminRestaurantReviewService;

class AdminRestaurantReviewServiceTest {

    @InjectMocks
    private AdminRestaurantReviewService reviewService;

    @Mock
    private AdminRestaurantReviewDao reviewDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReviews() {
        // Mock data
        AdminRestaurantReview review1 = new AdminRestaurantReview();
        review1.setReviewId(1);
        review1.setRestaurantId(101);

        AdminRestaurantReview review2 = new AdminRestaurantReview();
        review2.setReviewId(2);
        review2.setRestaurantId(101);

        // Mock behavior
        when(reviewDao.findByRestaurantId(101)).thenReturn(Arrays.asList(review1, review2));

        // Call the method
        List<AdminRestaurantReview> reviews = reviewService.getReviews(101);

        // Assertions
        assertNotNull(reviews);
        assertEquals(2, reviews.size());
        assertEquals(1, reviews.get(0).getReviewId());
        assertEquals(2, reviews.get(1).getReviewId());
        verify(reviewDao, times(1)).findByRestaurantId(101);
    }

    @Test
    void testUpdateResponse_Success() {
        // Mock data
        AdminRestaurantReview review = new AdminRestaurantReview();
        review.setReviewId(1);
        review.setReviewResponse("Updated Response");

        // Mock behavior
        when(reviewDao.save(review)).thenReturn(review);

        // Call the method
        Optional<AdminRestaurantReview> updatedReview = reviewService.updateResponse(review);

        // Assertions
        assertTrue(updatedReview.isPresent());
        assertEquals("Updated Response", updatedReview.get().getReviewResponse());
        verify(reviewDao, times(1)).save(review);
    }

    @Test
    void testUpdateResponse_Failure() {
        // Call the method with a null review
        Optional<AdminRestaurantReview> updatedReview = reviewService.updateResponse(null);

        // Assertions
        assertFalse(updatedReview.isPresent());
        verify(reviewDao, never()).save(any());
    }
}

package com.foodexpress.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodexpress.customer.model.RestaurantReview;
import com.foodexpress.customer.service.RestaurantReviewService;

@RestController
public class RestaurantReviewController {

    @Autowired
    private RestaurantReviewService restaurantReviewService;

    @GetMapping("get-restaurant-reviews/{restaurantId}")
    public List<RestaurantReview> getRestaurantReviewsHandler(@PathVariable int restaurantId) {
        return restaurantReviewService.getRestaurantReviews(restaurantId);
    }

    @PostMapping("add-review")
    public RestaurantReview giveReviewHandler(@RequestBody RestaurantReview restaurantReview) {
        return restaurantReviewService.giveReview(restaurantReview);
    }

    @PutMapping("update-review")
    public RestaurantReview updateReviewHandler(@RequestBody RestaurantReview restaurantReview) {
        return restaurantReviewService.updateReview(restaurantReview);
    }

    @GetMapping("get-average-rating/{restaurantId}")
    public double getAvgRestaurantRatingHandler(@PathVariable("restaurantId") int restaurantId) {
        return restaurantReviewService.getAvgRestaurantRating(restaurantId);
    }
    @GetMapping("get-review-for-edit/{userId}/{restaurantId}/{orderId}")
    public ResponseEntity<RestaurantReview> getReviewByUserAndRestaurantAndOrderHandler(
            @PathVariable("userId") Integer userId,
            @PathVariable("restaurantId") Integer restaurantId,
            @PathVariable("orderId") Integer orderId) {
        // Fetch the review
        RestaurantReview review = restaurantReviewService.getRestaurantReviewForEdit(userId, restaurantId, orderId);
        
        // If review is not found, return 404
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        
        // If found, return the review
        return ResponseEntity.ok(review);
    }
    
    @GetMapping("check-review/{userId}/{restaurantId}")
    public ResponseEntity<String> checkReviewExistsHandler(@PathVariable("userId") int userId, 
                                                           @PathVariable("restaurantId") int restaurantId) {
        boolean flag = restaurantReviewService.checkReviewExists(userId, restaurantId);
        if (flag) {
            return ResponseEntity.ok("Review exists");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review does not exist");
        }
    }

}

package com.foodexpress.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.foodexpress.admin.model.RestaurantReview;
import com.foodexpress.admin.service.RestaurantReviewService;
import java.util.List;
import java.util.Optional;

@RestController
public class RestaurantReviewController {

    @Autowired
    private RestaurantReviewService restaurantReviewService;

    @PostMapping("/get-reviews/{restaurantId}")
    public List<RestaurantReview> getReviewHandler(@PathVariable("restaurantId") int restaurantId) {
        // Fetch and return the list of reviews for the given restaurant ID
        return restaurantReviewService.getReviews(restaurantId);
    }

    @PostMapping("/update-review")
    public ResponseEntity<RestaurantReview> updateReviewHandler(@RequestBody RestaurantReview review) {
        Optional<RestaurantReview> updatedReview = restaurantReviewService.updateResponse(review);
        if (updatedReview.isPresent()) {
            return ResponseEntity.ok(updatedReview.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

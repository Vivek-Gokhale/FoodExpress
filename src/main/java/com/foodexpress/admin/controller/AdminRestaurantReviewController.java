package com.foodexpress.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.foodexpress.admin.model.AdminRestaurantReview;
import com.foodexpress.admin.service.AdminRestaurantReviewService;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminRestaurantReviewController {

    @Autowired
    private AdminRestaurantReviewService restaurantReviewService;

    @GetMapping("/get-reviews/{restaurantId}")
    public List<AdminRestaurantReview> getReviewHandler(@PathVariable("restaurantId") int restaurantId) {
        // Fetch and return the list of reviews for the given restaurant ID
        return restaurantReviewService.getReviews(restaurantId);
    }

    @PostMapping("/update-review")
    public ResponseEntity<AdminRestaurantReview> updateReviewHandler(@RequestBody AdminRestaurantReview review) {
        Optional<AdminRestaurantReview> updatedReview = restaurantReviewService.updateResponse(review);
        if (updatedReview.isPresent()) {
            return ResponseEntity.ok(updatedReview.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

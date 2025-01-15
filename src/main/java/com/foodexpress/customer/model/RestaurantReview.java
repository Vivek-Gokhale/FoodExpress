package com.foodexpress.customer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant_reviews")
public class RestaurantReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false, updatable = false)
    private Integer reviewId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "restaurant_id", nullable = false)
    private Integer restaurantId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "review_response")
    private String reviewResponse;
    
    @Transient
    private String usernameString;
}

package com.foodexpress.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.foodexpress.admin.model.RestaurantReview;
import java.util.List;

@Repository
public interface RestaurantReviewDao extends JpaRepository<RestaurantReview, Integer> {

   
    List<RestaurantReview> findByRestaurantId(Integer restaurantId);
}
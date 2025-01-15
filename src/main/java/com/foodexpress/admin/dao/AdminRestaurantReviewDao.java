package com.foodexpress.admin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.foodexpress.admin.model.AdminRestaurantReview;
import java.util.List;

@Repository
public interface AdminRestaurantReviewDao extends JpaRepository<AdminRestaurantReview, Integer> {

   
    List<AdminRestaurantReview> findByRestaurantId(Integer restaurantId);
}
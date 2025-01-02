package com.foodexpress.admin.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodexpress.admin.model.RestaurantAdmin;

public interface RestaurantAdminDao extends JpaRepository<RestaurantAdmin, Integer>{
	List<RestaurantAdmin> findAllByRestaurantId(int restaurantId);
	Optional<RestaurantAdmin> findByRestaurantIdAndEmailAndPassword(int restaurantId, String email, String password);
	
	@Query(value = "SELECT COUNT(DISTINCT o.user_id) " +
            "FROM orders o " +
            "WHERE o.user_id IN (" +
            "    SELECT DISTINCT user_id " +
            "    FROM order_items " +
            "    WHERE restaurant_id = :restaurantId" +
            ")",
    nativeQuery = true)
int countDistinctUsersByRestaurantId(@Param("restaurantId") int restaurantId);

	
	@Query(value = "SELECT COALESCE(COUNT(item_id), 0) AS item_count " +
            "FROM menu_items " +
            "WHERE restaurant_id = :restaurantId", 
    nativeQuery = true)
	int countItemsByRestaurantId(@Param("restaurantId") int restaurantId);
	
	@Query(value = "SELECT COALESCE(CEIL(AVG(rating)), 0) AS average_rating " +
	            "FROM restaurant_reviews " +
	            "WHERE restaurant_id = :restaurantId", 
	    nativeQuery = true)
	int findAvgRatingWithCeil(@Param("restaurantId") int restaurantId);
	
	@Query(value = "SELECT COALESCE(SUM(oi.price * oi.quantity), 0) AS total_price " +
            "FROM order_items oi " +
            "WHERE oi.restaurant_id = :restaurantId " +
            "AND oi.order_id IN ( " +
            "    SELECT o.order_id FROM orders o WHERE o.delivery_status = 'completed' " +
            ")", nativeQuery = true)
	int findTotalPriceByRestaurantId(@Param("restaurantId") int restaurantId);


	@Query(value = "SELECT name FROM restaurant_register_details WHERE restaurant_id = :restaurantId", 
		       nativeQuery = true)
		String findRestaurantNameById(@Param("restaurantId") int restaurantId);
	
	@Query(value = "SELECT location FROM restaurant_register_details WHERE restaurant_id = :restaurantId", 
		       nativeQuery = true)
		String findRestaurantLocationById(@Param("restaurantId") int restaurantId);



}

package com.foodexpress.admin.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodexpress.admin.model.RestaurantAdmin;

public interface RestaurantAdminDao extends JpaRepository<RestaurantAdmin, Integer>{
	List<RestaurantAdmin> findAllByRestaurantId(int restaurantId);
	Optional<RestaurantAdmin> findByRestaurantIdAndEmailAndPassword(int restaurantId, String email, String password);
}

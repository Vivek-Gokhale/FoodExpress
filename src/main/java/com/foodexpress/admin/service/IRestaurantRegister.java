package com.foodexpress.admin.service;

import java.util.Optional;

import com.foodexpress.admin.model.RestaurantRegister;

public interface IRestaurantRegister {
	public int registerRestaurant(RestaurantRegister restaurant);
	public boolean updateRestaurant(RestaurantRegister restaurant);
	public RestaurantRegister getRestaurant(int restaurantId);
	public int getRestaurantId(String email);
	
}

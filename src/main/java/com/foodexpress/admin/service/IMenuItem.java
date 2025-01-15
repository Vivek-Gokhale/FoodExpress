package com.foodexpress.admin.service;

import java.awt.MenuItem;
import java.util.List;
import java.util.Optional;

import com.foodexpress.admin.dto.FoodItems;
import com.foodexpress.admin.model.RestaurantMenuItem;

public interface IMenuItem {
	public Optional<RestaurantMenuItem> addMenuItem(RestaurantMenuItem item);
	public Optional<RestaurantMenuItem> updateMenuItem(RestaurantMenuItem item);
	public boolean deleteMenuItem(int menuItem);
	public List<RestaurantMenuItem> getMenuItems(int restaurantId);
	public Optional<RestaurantMenuItem> getMenuItem(int itemId);
	
	public List<FoodItems> getFoodItems();
}

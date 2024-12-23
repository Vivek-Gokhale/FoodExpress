package com.foodexpress.admin.dao;

import java.awt.MenuItem;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.admin.model.RestaurantMenuItem;

@Repository
public interface MenuItemDao extends JpaRepository<RestaurantMenuItem, Integer>{
	public List<RestaurantMenuItem> findAllByRestaurantId(int restaurantId);
}

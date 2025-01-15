package com.foodexpress.admin.dao;

import java.awt.MenuItem;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodexpress.admin.model.RestaurantMenuItem;

@Repository
public interface MenuItemDao extends JpaRepository<RestaurantMenuItem, Integer>{
	public List<RestaurantMenuItem> findAllByRestaurantId(int restaurantId);
	
	@Query(value = "SELECT r.restaurant_id, r.name AS restaurant_name, r.email, r.phone_no, r.registration_date, r.start_time, r.close_time, r.address, r.location, m.item_id, m.name AS item_name, m.price, m.stock, m.description, m.images " +
            "FROM restaurant_register_details r " +
            "INNER JOIN menu_items m ON r.restaurant_id = m.restaurant_id", nativeQuery = true)
	List<Object[]> getRestaurantMenuDetails();
}

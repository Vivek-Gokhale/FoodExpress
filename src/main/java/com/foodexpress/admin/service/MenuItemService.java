package com.foodexpress.admin.service;


import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.admin.dao.RestaurantRegisterDao;
import com.foodexpress.admin.dto.FoodItems;
import com.foodexpress.admin.model.RestaurantMenuItem;
import com.foodexpress.customer.dao.RestaurantReviewDao;
import com.foodexpress.customer.service.RestaurantReviewService;

@Service
public class MenuItemService implements IMenuItem {

    @Autowired
    MenuItemDao menuItemDao;
    
    @Autowired
    RestaurantReviewService restaurantReviewService;
    
    @Override
    public Optional<RestaurantMenuItem> addMenuItem(RestaurantMenuItem item) {
        RestaurantMenuItem savedItem = menuItemDao.save(item);
        return Optional.of(savedItem);
    }

    @Override
    public Optional<RestaurantMenuItem> updateMenuItem(RestaurantMenuItem item) {
        if (menuItemDao.existsById(item.getItemId())) {
            RestaurantMenuItem updatedItem = menuItemDao.save(item);
            return Optional.of(updatedItem);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteMenuItem(int menuItemId) {
        if (menuItemDao.existsById(menuItemId)) {
            menuItemDao.deleteById(menuItemId);
            return true;
        }
        return false;
    }

    @Override
    public List<RestaurantMenuItem> getMenuItems(int restaurantId) {
        return menuItemDao.findAllByRestaurantId(restaurantId);
    }

	@Override
	public Optional<RestaurantMenuItem> getMenuItem(int itemId) {
		// TODO Auto-generated method stub
		Optional<RestaurantMenuItem> savedItem = menuItemDao.findById(itemId);
        return savedItem;
	}

	@Override
	public List<FoodItems> getFoodItems() {
		// TODO Auto-generated method stub
		 List<Object[]> results = menuItemDao.getRestaurantMenuDetails();

	        List<FoodItems> foodItems = new ArrayList<>();
	        for (Object[] result : results) {
	        	double rating = restaurantReviewService.getAvgRestaurantRating((Integer) result[0]);
	            FoodItems dto = new FoodItems(
	                (Integer) result[0], // restaurantId
	                (String) result[1], // restaurantName
	                (String) result[2], // email
	                (String) result[3], // phoneNo
	                (Date) result[4], // registrationDate
	                (Time) result[5], // startTime
	                (Time) result[6], // closeTime
	                (String) result[7], // address
	                (String) result[8], // location
	                (Integer) result[9], // itemId
	                (String) result[10], // itemName
	                (Integer) result[11], // price
	                (Integer) result[12], // stock
	                (String) result[13], // description
	                (String) result[14], // images
	                rating	
	            );
	            foodItems.add(dto);
	        }

	        return foodItems;
	}
}

package com.foodexpress.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.admin.model.RestaurantMenuItem;

@Service
public class MenuItemService implements IMenuItem {

    @Autowired
    MenuItemDao menuItemDao;

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
}

package com.foodexpress.admin.service;

import java.util.List;

import com.foodexpress.admin.model.RestaurantAdmin;

public interface IRestaurantAdmin {
	public boolean addAdmin(RestaurantAdmin admin);
	public boolean updateAdmin(RestaurantAdmin admin);
	public boolean deleteAdmin(int aid);
	public List<RestaurantAdmin> getAdmins(int restaurantId);
	public String authenticateAdmin(int rid, String aEmail, String password);
	
}

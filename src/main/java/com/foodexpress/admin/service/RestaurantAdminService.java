package com.foodexpress.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.RestaurantAdminDao;
import com.foodexpress.admin.dao.RestaurantRegisterDao;
import com.foodexpress.admin.dto.DashboardDTO;
import com.foodexpress.admin.model.RestaurantAdmin;
import com.foodexpress.utilities.PasswordUtils;

@Service
public class RestaurantAdminService implements IRestaurantAdmin {

    @Autowired
    private RestaurantAdminDao restaurantAdminDao;
    @Autowired
    private RestaurantRegisterDao restaurantRegisterDao; 

    @Override
    public boolean addAdmin(RestaurantAdmin admin) {
        // Encrypt the password before saving
        String hashedPassword = PasswordUtils.hashPassword(admin.getPassword());
        admin.setPassword(hashedPassword);
        RestaurantAdmin savedAdmin = restaurantAdminDao.save(admin);
        return savedAdmin != null;
    }

    @Override
    public boolean updateAdmin(RestaurantAdmin admin) {
        if (restaurantAdminDao.existsById(admin.getAdminId())) {
            RestaurantAdmin existingAdmin = restaurantAdminDao.findById(admin.getAdminId()).orElse(null);
            if (existingAdmin != null) {
                // Encrypt the new password before updating, if provided
                if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
                    String hashedPassword = PasswordUtils.hashPassword(admin.getPassword());
                    existingAdmin.setPassword(hashedPassword);
                }
                existingAdmin.setEmail(admin.getEmail());
                existingAdmin.setUsername(admin.getUsername());
                restaurantAdminDao.save(existingAdmin);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteAdmin(int aid) {
        if (restaurantAdminDao.existsById(aid)) {
            restaurantAdminDao.deleteById(aid);
            return true;
        }
        return false;
    }

    @Override
    public List<RestaurantAdmin> getAdmins(int restaurantId) {
        return restaurantAdminDao.findAllByRestaurantId(restaurantId);
    }

    @Override
    public String authenticateAdmin(int restaurantId, String aEmail, String password) {

      
        String hashedPassword = PasswordUtils.hashPassword(password);
        
       return restaurantAdminDao
            .findByRestaurantIdAndEmailAndPassword(restaurantId, aEmail, hashedPassword)
            .get().getUsername();
    }

    public DashboardDTO getDashBoardDTO(int rid) {
        // Fetch the count of distinct users for the restaurant
    	
        int totalPrice = restaurantAdminDao.findTotalPriceByRestaurantId(rid);
        int userCount = restaurantAdminDao.countDistinctUsersByRestaurantId(rid);
        int rating = restaurantAdminDao.findAvgRatingWithCeil(rid);
        int itemCnt = restaurantAdminDao.countItemsByRestaurantId(rid);
        String name = restaurantAdminDao.findRestaurantNameById(rid);
        String location = restaurantAdminDao.findRestaurantLocationById(rid);
        // Create a new DashboardDTO object and populate it with the fetched data
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setAvgRating(rating);
        dashboardDTO.setMenuItems(itemCnt);
        dashboardDTO.setRestaurantName(name);
        dashboardDTO.setTotalPrice(totalPrice);
        dashboardDTO.setUserCount(userCount);
        dashboardDTO.setLocation(location);
        
        // Return the populated DashboardDTO
        return dashboardDTO;
    }



   
}

package com.foodexpress.admin.controller;

import com.foodexpress.admin.model.RestaurantAdmin;
import com.foodexpress.admin.service.RestaurantAdminService;
import com.foodexpress.admin.service.RestaurantRegisterService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class RestaurantAdminController {

    @Autowired
    private RestaurantAdminService restaurantAdminService;

    @Autowired
    private RestaurantRegisterService restaurantRegisterService;
    @PostMapping("add-admin")
    public ResponseEntity<String> addAdmin(@RequestBody RestaurantAdmin admin) {
        boolean isAdded = restaurantAdminService.addAdmin(admin);
        if (isAdded) {
        	
            return ResponseEntity.ok("Admin added successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to add admin.");
        }
    }

    @PostMapping("update-admin")
    public ResponseEntity<String> updateAdmin(@RequestBody RestaurantAdmin admin) {
        
        boolean isUpdated = restaurantAdminService.updateAdmin(admin);
        if (isUpdated) {
            return ResponseEntity.ok("Admin updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update admin.");
        }
    }

    @PostMapping("delete-admin/{aid}")
    public ResponseEntity<String> deleteAdmin(@PathVariable int aid) {
        boolean isDeleted = restaurantAdminService.deleteAdmin(aid);
        if (isDeleted) {
            return ResponseEntity.ok("Admin deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete admin.");
        }
    }

    @PostMapping("authenticate-admin")
    public ResponseEntity<String> authenticateAdmin(@RequestBody Map<String, String> requestBody) {
        String rEmail = requestBody.get("rEmail");
        String aEmail = requestBody.get("aEmail");
        String password = requestBody.get("password");
       
        if (rEmail == null || aEmail == null || password == null) {
            return ResponseEntity.badRequest().body("Invalid input");
        }
        int rid = restaurantRegisterService.getRestaurantId(rEmail);
        
        boolean isAuthenticated = restaurantAdminService.authenticateAdmin(rid, aEmail, password);

        if (isAuthenticated) {
            return ResponseEntity.ok("Authentication successful");
        } else {
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }

    
    @PostMapping("get-admins/{rid}")
    public ResponseEntity<List<RestaurantAdmin>> getAdminsHandler(@PathVariable("rid") int rid) {
        List<RestaurantAdmin> admins = restaurantAdminService.getAdmins(rid);

        if (admins.isEmpty()) {
            return ResponseEntity.status(404).body(null); 
        }

        return ResponseEntity.ok(admins); 
    }



    
    
}

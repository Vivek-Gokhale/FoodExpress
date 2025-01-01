package com.foodexpress.admin.controller;

import com.foodexpress.admin.dto.DashboardDTO;
import com.foodexpress.admin.model.RestaurantAdmin;
import com.foodexpress.admin.service.RestaurantAdminService;
import com.foodexpress.admin.service.RestaurantRegisterService;

import java.util.HashMap;
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
    	System.out.println(admin);
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

    @DeleteMapping("delete-admin/{aid}")
    public ResponseEntity<String> deleteAdmin(@PathVariable int aid) {
        boolean isDeleted = restaurantAdminService.deleteAdmin(aid);
        if (isDeleted) {
            return ResponseEntity.ok("Admin deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete admin.");
        }
    }

    @PostMapping("authenticate-admin")
    public ResponseEntity<Map<String, Object>> authenticateAdmin(@RequestBody Map<String, String> requestBody) {
        String rEmail = requestBody.get("rEmail");
        String aEmail = requestBody.get("aEmail");
        String password = requestBody.get("password");

        if (rEmail == null || aEmail == null || password == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid input");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        int rid = restaurantRegisterService.getRestaurantId(rEmail);

        String name = restaurantAdminService.authenticateAdmin(rid, aEmail, password);

        if (!name.isEmpty()) {
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("botName", name);
            successResponse.put("restaurantId", rid);
            return ResponseEntity.ok(successResponse);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Authentication failed");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }


    
    @GetMapping("get-admins/{rid}")
    public ResponseEntity<List<RestaurantAdmin>> getAdminsHandler(@PathVariable("rid") int rid) {
        List<RestaurantAdmin> admins = restaurantAdminService.getAdmins(rid);

        if (admins.isEmpty()) {
            return ResponseEntity.status(404).body(null); 
        }

        return ResponseEntity.ok(admins); 
    }
    
    @GetMapping("get-dashboard/{restaurantId}")
    public ResponseEntity<DashboardDTO> getDashboardDTO(@PathVariable("restaurantId") int restaurantId) {
        DashboardDTO dashboardDTO = restaurantAdminService.getDashBoardDTO(restaurantId);
        if (dashboardDTO != null) {
            return ResponseEntity.ok(dashboardDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    
    
}

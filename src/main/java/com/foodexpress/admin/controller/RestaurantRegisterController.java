package com.foodexpress.admin.controller;

import com.foodexpress.admin.model.RestaurantRegister;
import com.foodexpress.admin.service.IRestaurantRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestaurantRegisterController {

    @Autowired
    private IRestaurantRegister restaurantService;

    @PostMapping("/restaurant-register")
    public ResponseEntity<Integer> registerRestaurantHandler(@RequestBody RestaurantRegister restaurant) {
        int restaurantId = restaurantService.registerRestaurant(restaurant);
        return ResponseEntity.ok(restaurantId > 0 ? restaurantId : -1);
    }


    @PostMapping("/restaurant-update")
    public ResponseEntity<String> updateRestaurantHandler(@RequestBody RestaurantRegister restaurant) {
        boolean isUpdated = restaurantService.updateRestaurant(restaurant);
        if (isUpdated) {
            return ResponseEntity.ok("Restaurant updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update the restaurant.");
        }
    }

    @PostMapping("get-restaurant/{restaurantId}")
    public ResponseEntity<Object> getRestaurantHandler(@PathVariable int restaurantId) {
        RestaurantRegister restaurant = restaurantService.getRestaurant(restaurantId);
        if (restaurant != null) {
            return ResponseEntity.ok(restaurant);
        } else {
            return ResponseEntity.status(404).body("Restaurant not found.");
        }
    }
}

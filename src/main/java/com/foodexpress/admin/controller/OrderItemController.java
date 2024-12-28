package com.foodexpress.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodexpress.admin.dto.CustomerMenuPreferenceStats;
import com.foodexpress.admin.model.OrderItem;
import com.foodexpress.admin.service.OrderItemService;

@RestController
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

   
    @GetMapping("/get-order-items/{restaurantId}")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable int restaurantId) {
        List<OrderItem> orderItems = orderItemService.getOrderItems(restaurantId);
        if (orderItems.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(orderItems); // 200 OK
    }

    @PutMapping("/update-order-status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody OrderItem orderItem) {
        boolean isUpdated = orderItemService.updateOrderStatus(orderItem);
        if (isUpdated) {
            return ResponseEntity.ok("Order status updated successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to update order status. Invalid flag or order item ID.");
        }
    }
    
    @GetMapping("get-customer-preferences/{restaurantId}")
    public List<CustomerMenuPreferenceStats> getMenuPreferences(@PathVariable int restaurantId) {
        return orderItemService.getMenuPreferencesForRestaurant(restaurantId);
    }
}

package com.foodexpress.admin.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.foodexpress.utilities.FileUploadHelper;
import com.foodexpress.utilities.ImageStringToList;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.foodexpress.admin.dto.FoodItems;
import com.foodexpress.admin.model.RestaurantMenuItem;
import com.foodexpress.admin.service.MenuItemService;

@RestController
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("add-menu-item")
    public ResponseEntity<String> addMenuItem(
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("stock") int stock,
            @RequestParam("description") String description,
            @RequestParam("images") MultipartFile[] files,
            @RequestParam("restaurantId") int restaurantId) {
        
       

        // Validate uploaded files
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body("No files were uploaded.");
        }

        // Check if files are images
        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed.");
            }
        }

        // Upload files and get the list of unique filenames
        List<String> uploadedFileNames;
        try {
            uploadedFileNames = FileUploadHelper.uploadFiles(files);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred during file upload. Please try again.");
        }

        if (uploadedFileNames.isEmpty()) {
            return ResponseEntity.badRequest().body("File upload failed. Please try again.");
        }

        // Join the uploaded filenames into a comma-separated string
        String fileNames = String.join(",", uploadedFileNames);

        // Create and set the properties of the RestaurantMenuItem object
        RestaurantMenuItem item = new RestaurantMenuItem();
        item.setName(name);
        item.setPrice(price);
        item.setStock(stock);
        item.setDescription(description);
        item.setImages(fileNames); // Set the uploaded file names
        item.setRestaurantId(restaurantId);

        // Save the menu item to the database
        Optional<RestaurantMenuItem> savedItem = menuItemService.addMenuItem(item);

        if (savedItem.isPresent()) {
            return ResponseEntity.ok("Menu item added successfully with files: " + fileNames);
        } else {
            return ResponseEntity.badRequest().body("Failed to save the menu item.");
        }
    }




    @PostMapping("edit-menu-item/{itemId}")
    public ResponseEntity<RestaurantMenuItem> updateMenuItem(@PathVariable int itemId, @RequestBody RestaurantMenuItem item) {
        item.setItemId(itemId); // Ensure the correct item ID is set
        Optional<RestaurantMenuItem> updatedItem = menuItemService.updateMenuItem(item);
        return updatedItem
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("delete-menu-item/{itemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable int itemId) {
        boolean isDeleted = menuItemService.deleteMenuItem(itemId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("get-menu-items/{restaurantId}")
    public ResponseEntity<List<RestaurantMenuItem>> getMenuItems(@PathVariable int restaurantId) {
        List<RestaurantMenuItem> items = menuItemService.getMenuItems(restaurantId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("get-menu-item/{itemId}")
    public ResponseEntity<RestaurantMenuItem> getMenuItem(@PathVariable("itemId") int itemId) {
        
        RestaurantMenuItem menuItem = menuItemService.getMenuItem(itemId).get();

        
        if (menuItem != null) {
            return ResponseEntity.ok(menuItem); // 200 OK with the menu item in the body
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
    
    @GetMapping("get-food-items")
    public ResponseEntity<List<FoodItems>> getFoodItems()
    {
    	List<FoodItems> foodItems = menuItemService.getFoodItems();
    	return ResponseEntity.ok(foodItems);
    }
    

}

package com.foodexpress.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.OrderItemDao;
import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.admin.model.OrderItem;
import com.foodexpress.admin.dao.RestaurantRegisterDao;
import com.foodexpress.admin.model.RestaurantRegister;
import com.foodexpress.admin.model.RestaurantMenuItem;

@Service
public class OrderItemService implements IOrderItem {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private MenuItemDao menuItemDao;

    @Autowired
    private RestaurantRegisterDao restaurantRegisterDao;

    @Override
    public List<OrderItem> getOrderItems(int restaurantId) {
        // Fetch order items for the specified restaurant
        List<OrderItem> orderItems = orderItemDao.findByRestaurantId(restaurantId);

        // Populate restaurantName and itemName fields
        return orderItems.stream().map(orderItem -> {
            // Set restaurantName
        	System.out.println("called1");
            RestaurantRegister restaurant = restaurantRegisterDao.findById(orderItem.getRestaurantId())
                    .orElse(null);
            System.out.println(restaurant);
            orderItem.setRestaurantName(restaurant != null ? restaurant.getName() : "Unknown Restaurant");

            // Set itemName
            RestaurantMenuItem menuItem = menuItemDao.findById(orderItem.getItemId()).orElse(null);
            System.out.println(menuItem);
            orderItem.setItemName(menuItem != null ? menuItem.getName() : "Unknown Item");

            return orderItem;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean updateOrderStatus(OrderItem orderItem) {
        orderItemDao.save(orderItem); // Save the updated order item
        return true;
    }
}

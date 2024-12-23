package com.foodexpress.customer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.customer.model.Order;
import com.foodexpress.admin.dao.OrderItemDao;
import com.foodexpress.admin.dao.RestaurantRegisterDao;
import com.foodexpress.admin.model.OrderItem;
import com.foodexpress.customer.dao.OrderDao;

@Service
public class OrderService implements IOrder {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    MenuItemDao menuItemDao;

    @Autowired
    RestaurantRegisterDao restaurantRegisterDao;

    @Override
    public List<Order> getOrders(int userId) {
        // Fetch all orders for the user
        List<Order> orders = orderDao.findByUserId(userId);

        // For each order, fetch order items and add to the order
        return orders.stream().map(order -> {
            List<OrderItem> orderItems = orderItemDao.findByOrderId(order.getOrderId());
            
            // Set order items for the order
            order.setOrderItems(orderItems);

            

            // For each order item, set the item name
            orderItems.forEach(orderItem -> {
            	String restaurantName = getRestaurantNameById(orderItem.getRestaurantId());
                orderItem.setRestaurantName(restaurantName);
                
                String itemName = getItemNameById(orderItem.getItemId());
                orderItem.setItemName(itemName);
            });

            return order;
        }).collect(Collectors.toList());
    }

    // Method to fetch restaurant name by restaurantId
    private String getRestaurantNameById(int restaurantId) {
        return restaurantRegisterDao.findById(restaurantId)
            .map(restaurant -> restaurant.getName())
            .orElse("Unknown Restaurant"); // Default value if not found
    }

    // Method to fetch item name by itemId
    private String getItemNameById(int itemId) {
        return menuItemDao.findById(itemId)
            .map(menuItem -> menuItem.getName())
            .orElse("Unknown Item"); // Default value if not found
    }
}

package com.foodexpress.customer.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.customer.model.AppliedCoupon;
import com.foodexpress.customer.model.Cart;
import com.foodexpress.customer.model.Customer;
import com.foodexpress.customer.model.Order;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;
import com.foodexpress.admin.dao.OrderItemDao;
import com.foodexpress.admin.dao.RestaurantRegisterDao;
import com.foodexpress.admin.model.OrderItem;
import com.foodexpress.admin.model.RestaurantMenuItem;
import com.foodexpress.customer.dao.AppliedCouponDao;
import com.foodexpress.customer.dao.CartDao;
import com.foodexpress.customer.dao.CustomerDao;
import com.foodexpress.customer.dao.OrderDao;

@Service
public class OrderService implements IOrder {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private MenuItemDao menuItemDao;

    @Autowired
    private RestaurantRegisterDao restaurantRegisterDao;
    
    @Autowired
    private AppliedCouponDao appliedCouponDao;
    
    @Autowired
    private CartDao cartDao;
    
    @Autowired
    private CustomerDao customerDao;
    
    

    @Override
    public List<Order> getOrders(int userId) {
        // Fetch all orders for the user
        List<Order> orders = orderDao.findAllByUserId(userId);

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
                
                String image = getItemImageById(orderItem.getItemId());
                orderItem.setImage(image);
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
    
    private String getItemImageById(int itemId) {
        return menuItemDao.findById(itemId)
            .map(menuItem -> menuItem.getImages())
            .orElse("Unknown Item"); // Default value if not found
    }

    @Override
    public Order placeOrder(Integer userId, Integer paymentFlag, double finalAmount, Map<Integer, String> itemRequestMap) {
        // Fetch applied coupons for the user
        List<AppliedCoupon> appliedCoupons = appliedCouponDao.findByUserId(userId);
        List<Cart> carts = cartDao.findByUserId(userId);
        List<String> couponIds = new ArrayList<>();
        String jsonCouponIds;

        for (AppliedCoupon appliedCoupon : appliedCoupons) {
            couponIds.add(String.valueOf(appliedCoupon.getAppliedCouponsId()));
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonCouponIds = objectMapper.writeValueAsString(couponIds);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setAmount(finalAmount);
        order.setCouponIds(jsonCouponIds);
        order.setPaymentOption(paymentFlag == 1 ? "online" : "cod");
        order.setDeliveryStatus("pending");
        order.setOrderDate(new java.sql.Date(System.currentTimeMillis()));

        Order placedOrder = orderDao.save(order);
        if (placedOrder == null) {
            return null;
        }

        List<RestaurantMenuItem> menuItems = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cart : carts) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCurrStatus("placed");
            orderItem.setOrderId(placedOrder.getOrderId());
            orderItem.setItemId(cart.getItemId());
            orderItem.setSpecialRequest(itemRequestMap.get(cart.getItemId()));
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setRestaurantId(menuItemDao.findById(cart.getItemId()).orElseThrow().getRestaurantId());
            orderItem.setPrice(menuItemDao.findById(cart.getItemId()).orElseThrow().getPrice());

            
            RestaurantMenuItem menuItem = menuItemDao.findById(cart.getItemId()).get();
            menuItem.setStock(menuItem.getStock()-cart.getQuantity());
            
            menuItems.add(menuItem);
            orderItems.add(orderItem);
        }

        
         boolean flag = orderItemDao.saveAll(orderItems) != null;
         if(flag)
         {	menuItemDao.saveAll(menuItems);
        	 
         }
         
         
         appliedCouponDao.deleteAll(appliedCouponDao.findByUserId(userId));
         cartDao.deleteAll(cartDao.findByUserId(userId));
         return placedOrder;
    }

	@Override
	public List<DeliveryPartner> getDeliveryStatus(Integer orderId) {
		// TODO Auto-generated method stub
		return orderDao.getDeliveryPartner(orderId);
	}

	@Override
	public Customer getCustomer(Integer userId) {
		// TODO Auto-generated method stub
		return customerDao.findById(userId).get();
	}

	

}

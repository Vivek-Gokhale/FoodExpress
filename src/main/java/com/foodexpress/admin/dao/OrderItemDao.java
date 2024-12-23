package com.foodexpress.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.admin.model.OrderItem;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Integer>{
	public List<OrderItem> findByRestaurantId(Integer restaurantId);
	public List<OrderItem> findByOrderId(Integer orderId);
 }

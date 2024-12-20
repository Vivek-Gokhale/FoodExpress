package com.foodexpress.customer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.customer.model.Order;

import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
    
    // Method to find a list of orders based on userId
    List<Order> findByUserId(int userId);
}

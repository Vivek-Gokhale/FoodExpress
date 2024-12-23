package com.foodexpress.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.customer.model.Order;
import com.foodexpress.customer.service.OrderService;

@RestController
public class OrderController {

	@Autowired
	OrderService orderService;
	@GetMapping("get-orders/{userId}")
	public List<Order> getOrders(@PathVariable("userId") int userId)
	{
		return orderService.getOrders(userId);
	}
	
}

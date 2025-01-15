package com.foodexpress.customer.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.customer.model.CustomerOrderItem;
import com.foodexpress.customer.service.CustomerOrderItemService;

@RestController
public class CustomerOrderItemController {

	@Autowired
	private CustomerOrderItemService customerOrderItemService;
	
	@GetMapping("get-modifiable-order-items/{userId}")
	public List<CustomerOrderItem> getOrderItemsHandler(@PathVariable("userId") Integer userId)
	{
		return customerOrderItemService.getOrderItems(userId);
	}
	
	@PutMapping("update-order-items")
	public List<CustomerOrderItem> updateOrderItemHandler(@RequestBody List<CustomerOrderItem> items)
	{
		return customerOrderItemService.updateCustomerOrderItems(items);
	}
}

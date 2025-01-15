package com.foodexpress.customer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.model.OrderItem;
import com.foodexpress.customer.dao.CustomerOrderItemDao;
import com.foodexpress.customer.model.CustomerOrderItem;

@Service
public class CustomerOrderItemService implements ICustomerOrderItem{

	
	@Autowired
	private CustomerOrderItemDao customerOrderItemDao;
	@Override
	public List<CustomerOrderItem> getOrderItems(Integer userId) {
		// TODO Auto-generated method stub
		return customerOrderItemDao.findOrderItemsByUserIdAndStatus(userId);
	}
	@Override
	public List<CustomerOrderItem> updateCustomerOrderItems(List<CustomerOrderItem> items) {
		// TODO Auto-generated method stub
		return customerOrderItemDao.saveAll(items);
	}

}

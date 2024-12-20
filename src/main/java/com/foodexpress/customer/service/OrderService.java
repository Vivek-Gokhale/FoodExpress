package com.foodexpress.customer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.customer.dao.OrderDao;
import com.foodexpress.customer.model.Order;

@Service
public class OrderService implements IOrder{

	@Autowired
	OrderDao orderDao;

	@Override
	public List<Order> getOrders(int userId) {
		// TODO Auto-generated method stub
		return orderDao.findByUserId(userId);
		//adding restaurant ids and item id data is remaining that will be after admin module devel
	}
	
}

package com.foodexpress.deliveryPartner.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.customer.dao.CustomerDao;
import com.foodexpress.customer.dao.OrderDao;
import com.foodexpress.customer.model.Customer;
import com.foodexpress.customer.model.Order;
import com.foodexpress.deliveryPartner.dao.DeliveryOrderDao;
import com.foodexpress.deliveryPartner.dao.DeliveryPartnerDao;
import com.foodexpress.deliveryPartner.dto.OrderStatus;
import com.foodexpress.deliveryPartner.model.DeliveryOrder;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;
import com.foodexpress.utilities.EmailService;

@Service
public class DeliveryOrderService implements IDeliveryOrder{

	@Autowired
	private DeliveryOrderDao deliveryOrderDao;

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private DeliveryPartnerDao deliveryPartnerDao;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public boolean selectDeliveryOrder(Integer partnerId, Integer orderId, String longitude, String latitude) {
		// TODO Auto-generated method stub
		DeliveryOrder deliveryOrder = new DeliveryOrder();
	    
	    deliveryOrder.setDeliveryPartnerId(partnerId);
	    deliveryOrder.setOrderId(orderId);
	    deliveryOrder.setStatus("pickedup");
	    
	    // Save the new instance to the database
	    deliveryOrderDao.save(deliveryOrder);
	    
	    Order order =  orderDao.findById(orderId).get();
	    order.setDeliveryStatus("pickedup");
	    orderDao.save(order);
	    
	    DeliveryPartner deliveryPartner = deliveryPartnerDao.findById(partnerId).get();
	    deliveryPartner.setCurrLocLatitude(latitude);
	    deliveryPartner.setCurrLocLongitude(longitude);
	    deliveryPartner.setIsAvail(false);
	    deliveryPartnerDao.save(deliveryPartner);
	    
	  
	    return true;
	}

	@Override
	public boolean verifyOTP(Integer deliveryId, Integer userId) {
	    
	    DeliveryOrder order = deliveryOrderDao.findById(deliveryId)
	        .orElseThrow(() -> new IllegalArgumentException("Delivery order not found for ID " + deliveryId));
	    System.out.println(order);
	    DeliveryPartner partner = deliveryPartnerDao.findById(order.getDeliveryPartnerId())
	        .orElseThrow(() -> new IllegalArgumentException("Delivery partner not found for ID " + order.getDeliveryId()));

	    partner.setIsAvail(true);
	    deliveryPartnerDao.save(partner);

	    order.setStatus("delivered");
	    deliveryOrderDao.save(order);

	    Order order2 = orderDao.findById(order.getOrderId())
	        .orElseThrow(() -> new IllegalArgumentException("Order not found for ID " + order.getOrderId()));

	    order2.setDeliveryStatus("completed");
	    orderDao.save(order2);

	    return true;
	}


	@Override
	public List<OrderStatus> getPendingOrders(Integer partnerId) {
	    // Fetch raw query results
	    List<Object[]> rawResults = deliveryOrderDao.findPendingOrdersByDeliveryPartner(partnerId);

	    // Map raw results to OrderStatus DTOs
	    return rawResults.stream()
	        .map(this::mapToOrderStatus)
	        .collect(Collectors.toList());
	}

	@Override
	public List<OrderStatus> getCompletedOrders(Integer partnerId) {
	    // Fetch raw query results
	    List<Object[]> rawResults = deliveryOrderDao.findCompletedOrdersByDeliveryPartner(partnerId);

	    // Map raw results to OrderStatus DTOs
	    return rawResults.stream()
	        .map(this::mapToOrderStatus)
	        .collect(Collectors.toList());
	}

	
	private OrderStatus mapToOrderStatus(Object[] row) {
	    return new OrderStatus(
	        (Integer) row[0], // deliveryId
	        (Integer) row[1], // orderId
	        (Integer) row[2], // userId
	        (String) row[3],  // userName
	        (String) row[4],  // phoneNo
	        (String) row[5],  // address
	        (Integer) row[6],  // totalAmount
	        (String) row[7]   // paymentOption
	    );
	}

	@Override
	public Customer getCustomer(Integer userId) {
		// TODO Auto-generated method stub
		return customerDao.findById(userId).get();
	}

	@Override
	public Customer getCustomerByOrderId(Integer orderId) {
		// TODO Auto-generated method stub
		return  customerDao.findById(orderDao.findById(orderId).get().getUserId()).get();
	}

	public boolean checkLiveTrackingCondition(int orderId) {
	    return deliveryOrderDao.findByOrderId(orderId) != null;
	}

	public Object[] getDeliveryPartner(int orderId) {
	    Object[] result = deliveryOrderDao.findDeliveryPartnerByOrderId(orderId);
	    
	   

	    return result;
	}



}

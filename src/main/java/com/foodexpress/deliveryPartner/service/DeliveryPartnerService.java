package com.foodexpress.deliveryPartner.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.customer.dao.CustomerAddressDao;
import com.foodexpress.customer.dao.CustomerDao;
import com.foodexpress.customer.dao.OrderDao;
import com.foodexpress.customer.model.CustomerAddress;
import com.foodexpress.customer.model.Order;
import com.foodexpress.deliveryPartner.dao.DeliveryPartnerDao;
import com.foodexpress.deliveryPartner.dto.PendingOrderStatus;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;
import com.foodexpress.utilities.PasswordUtils;

@Service
public class DeliveryPartnerService implements IDeliveryPartner{

	@Autowired
	private DeliveryPartnerDao deliveryPartnerDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private CustomerAddressDao customerAddressDao;

	@Override
	public boolean updateLocation(Integer partnerId, String longitude, String latitude) {
		// TODO Auto-generated method stub
		DeliveryPartner deliveryPartner = deliveryPartnerDao.findById(partnerId).get();
		deliveryPartner.setCurrLocLatitude(latitude);
		deliveryPartner.setCurrLocLongitude(longitude);
		deliveryPartnerDao.save(deliveryPartner);
		return true;
	}

	@Override
	public DeliveryPartner registerDeliveryPartner(DeliveryPartner partner) {
		// TODO Auto-generated method stub
		if(deliveryPartnerDao.findByEmail(partner.getEmail()) == null)
		{
			String hashedPassword = PasswordUtils.hashPassword(partner.getPassword());
			partner.setPassword(hashedPassword);
			return deliveryPartnerDao.save(partner);
		}
		return null;
	}
	
	public DeliveryPartner authenticateDeliveryPartner(String email, String password) {
	    if (email == null || password == null) {
	        throw new IllegalArgumentException("Email or password cannot be null");
	    }
	    String hashedPassword = PasswordUtils.hashPassword(password);
	    return deliveryPartnerDao.findByEmailAndPassword(email, hashedPassword);
	}

	public List<PendingOrderStatus> getPendingOrders() {
	    // Fetch all orders
	    List<Order> orders = orderDao.findAll();
	    System.out.println(orders);

	    // Initialize a list to store pending order statuses
	    List<PendingOrderStatus> pendingOrderStatusList = new ArrayList<>();

	    for (Order order : orders) {
	        // Check if the delivery status is "pending"
	        if ("pending".equalsIgnoreCase(order.getDeliveryStatus())) { // Use equalsIgnoreCase to avoid potential issues with case sensitivity
	            PendingOrderStatus pendingOrderStatus = new PendingOrderStatus();
	            pendingOrderStatus.setOrderId(order.getOrderId());
	            pendingOrderStatus.setUserId(order.getUserId());

	            // Fetch all addresses for the user
	            List<CustomerAddress> addresses = customerAddressDao.findAllByUserId(order.getUserId());
	            System.out.println(addresses);

	            for (CustomerAddress address : addresses) {
	                // Check if the address is the default one
	                if (address.isDefault()) {
	                    // Format the address into a single string
	                    String formattedAddress = String.format(
	                        "%s, %s, %s, %s, %s, %s",
	                        address.getHouseApartment(),
	                        address.getArea(),
	                        address.getLandmark(),
	                        address.getTownCity(),
	                        address.getState(),
	                        address.getPincode()
	                    );

	                    pendingOrderStatus.setAddress(formattedAddress);
	                    System.out.println(pendingOrderStatus);
	                    break; // Exit the loop once the default address is found
	                }
	            }

	            // Add the pending order status to the list
	            pendingOrderStatusList.add(pendingOrderStatus);
	        }
	    }

	    return pendingOrderStatusList;
	}


	
}

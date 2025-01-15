package com.foodexpress.deliveryPartner.service;

import java.util.List;

import com.foodexpress.deliveryPartner.dto.PendingOrderStatus;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;

public interface IDeliveryPartner {
	public boolean updateLocation(Integer partnerId, String longitude, String latitude);
	public DeliveryPartner registerDeliveryPartner(DeliveryPartner partner);
	public DeliveryPartner authenticateDeliveryPartner(String email, String password);
	public List<PendingOrderStatus> getPendingOrders();
	
}

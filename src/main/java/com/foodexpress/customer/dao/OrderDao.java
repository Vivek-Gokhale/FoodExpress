package com.foodexpress.customer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foodexpress.customer.model.Order;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;

import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
    
    // Method to find a list of orders based on userId
  public  List<Order> findAllByUserId(int userId);
  @Query("SELECT dp FROM DeliveryPartner dp WHERE dp.deliveryPartnerId = " +
	       "(SELECT d.deliveryPartnerId FROM DeliveryOrder d WHERE d.orderId = :orderId)")
	List<DeliveryPartner> getDeliveryPartner(@Param("orderId") Integer orderId);


}

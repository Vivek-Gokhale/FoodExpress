package com.foodexpress.deliveryPartner.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foodexpress.deliveryPartner.dto.OrderStatus;
import com.foodexpress.deliveryPartner.model.DeliveryOrder;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;

@Repository
public interface DeliveryOrderDao extends JpaRepository<DeliveryOrder, Integer>{
	
	public DeliveryOrder findByOrderId(Integer orderId);

	@Query(value = "SELECT " +
		       "d.delivery_id AS deliveryId, " +
		       "o.order_id AS orderId, " +
		       "o.user_id AS userId, " +
		       "CONCAT(crd.first_name, ' ', crd.last_name) AS userName, " +
		       "crd.phone_no AS phoneNo, " +
		       "CONCAT(ca.house_apartment, ', ', ca.area, ', ', " +
		       "IFNULL(ca.landmark, ''), ', ', ca.towncity, ', ', ca.state, ', ', ca.pincode) AS address, " +
		       "o.amount AS totalAmount, " +
		       "o.payment_option AS paymentOption " +
		       "FROM orders o " +
		       "JOIN customer_register_detail crd ON o.user_id = crd.user_id " +
		       "JOIN customer_address ca ON o.user_id = ca.user_id AND ca.is_default = 1 " +
		       "LEFT JOIN deliveries d ON o.order_id = d.order_id " +
		       "WHERE d.status = 'pickedup' " +
		       "AND d.delivery_partner_id = :deliveryPartnerId",
		       nativeQuery = true)
		List<Object[]> findPendingOrdersByDeliveryPartner(@Param("deliveryPartnerId") Integer deliveryPartnerId);


	@Query(value = "SELECT " +
		       "d.delivery_id AS deliveryId, " +
		       "o.order_id AS orderId, " +
		       "o.user_id AS userId, " +
		       "CONCAT(crd.first_name, ' ', crd.last_name) AS userName, " +
		       "crd.phone_no AS phoneNo, " +
		       "CONCAT(ca.house_apartment, ', ', ca.area, ', ', " +
		       "IFNULL(ca.landmark, ''), ', ', ca.towncity, ', ', ca.state, ', ', ca.pincode) AS address, " +
		       "o.amount AS totalAmount, " +
		       "o.payment_option AS paymentOption " +
		       "FROM orders o " +
		       "JOIN customer_register_detail crd ON o.user_id = crd.user_id " +
		       "JOIN customer_address ca ON o.user_id = ca.user_id AND ca.is_default = 1 " +
		       "LEFT JOIN deliveries d ON o.order_id = d.order_id " +
		       "WHERE d.status = 'delivered' " +
		       "AND d.delivery_partner_id = :deliveryPartnerId",
		       nativeQuery = true)
		List<Object[]> findCompletedOrdersByDeliveryPartner(@Param("deliveryPartnerId") Integer deliveryPartnerId);

		@Query(value = "SELECT * FROM delivery_partners dp WHERE dp.delivery_partner_id = (SELECT d.delivery_partner_id FROM deliveries d WHERE d.order_id = :orderId)", nativeQuery = true)
		Object[] findDeliveryPartnerByOrderId(@Param("orderId") int orderId);



}

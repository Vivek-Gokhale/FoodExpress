package com.foodexpress.customer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foodexpress.customer.model.CustomerOrderItem;

@Repository
public interface CustomerOrderItemDao extends JpaRepository<CustomerOrderItem,Integer>{
	@Query("SELECT coi FROM CustomerOrderItem coi WHERE coi.orderId IN (SELECT o.orderId FROM Order o WHERE o.userId = :userId) AND (coi.currStatus = 'placed' OR coi.currStatus = 'accept')")
	List<CustomerOrderItem> findOrderItemsByUserIdAndStatus(@Param("userId") Integer userId);
	

}

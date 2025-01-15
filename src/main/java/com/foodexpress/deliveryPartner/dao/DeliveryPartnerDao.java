package com.foodexpress.deliveryPartner.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.deliveryPartner.model.DeliveryPartner;
import java.util.List;


@Repository
public interface DeliveryPartnerDao extends JpaRepository<DeliveryPartner, Integer>{
	public DeliveryPartner findByEmail(String email);
	public DeliveryPartner findByEmailAndPassword(String email, String password);
}

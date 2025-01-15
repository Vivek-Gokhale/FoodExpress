package com.foodexpress.customer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.customer.model.Coupon;

@Repository
public interface CouponDao extends JpaRepository<Coupon, Integer>{
	public Coupon findByCode(String code);
}

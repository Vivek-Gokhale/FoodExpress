package com.foodexpress.customer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.customer.model.AppliedCoupon;

@Repository
public interface AppliedCouponDao extends JpaRepository<AppliedCoupon, Integer>{
	public List<AppliedCoupon> findByUserId(Integer userId);
}

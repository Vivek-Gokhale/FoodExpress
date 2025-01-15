package com.foodexpress.customer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.customer.model.Cart;

@Repository
public interface CartDao extends JpaRepository<Cart, Integer>{
	public List<Cart> findByUserId(Integer userId);
	public Cart findByUserIdAndItemId(Integer userId, Integer itemId);

}

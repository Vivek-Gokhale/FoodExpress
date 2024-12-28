package com.foodexpress.customer.service;

import java.util.List;

import com.foodexpress.customer.model.Cart;

public interface ICart {
	public List<Cart> addToCart(Cart cart);
	public List<Cart> updateCart(Cart cart, int flag);
	public List<Cart> removeFromCart(Integer cartItemId, Integer userId);
}

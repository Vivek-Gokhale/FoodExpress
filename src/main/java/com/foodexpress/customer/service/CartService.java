package com.foodexpress.customer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.customer.dao.CartDao;
import com.foodexpress.customer.model.Cart;

@Service
public class CartService implements ICart {

    @Autowired
    private CartDao cartDao;

    @Override
    public List<Cart> addToCart(Cart cart) {
        // Check if a record with the same userId and itemId exists
        Cart existingCart = cartDao.findByUserIdAndItemId(cart.getUserId(), cart.getItemId());
        if (existingCart != null) {
            // Update the quantity if the record exists
            existingCart.setQuantity(Math.min(cart.getStocks(),existingCart.getQuantity() + cart.getQuantity()));
            cartDao.save(existingCart);
        } else {
            // Insert a new record if no matching record exists
            cartDao.save(cart);
        }
        return cartDao.findByUserId(cart.getUserId());
    }

    @Override
    public List<Cart> updateCart(Cart cart, int flag) {
        // Check if a record with the same userId and itemId exists
        Cart existingCart = cartDao.findByUserIdAndItemId(cart.getUserId(), cart.getItemId());
        if (existingCart != null) {
            // Update the existing record
        	if(flag == 1)
        	{
        		 existingCart.setQuantity(Math.min(cart.getQuantity()+1, cart.getStocks()));
        	}
        	else {
        		 existingCart.setQuantity(Math.max(cart.getQuantity()-1, 1));
        	} 
           
            cartDao.save(existingCart);
        } else {
            // Insert a new record if no matching record exists
            cartDao.save(cart);
        }
        return cartDao.findByUserId(cart.getUserId());
    }

	@Override
	public List<Cart> removeFromCart(Integer cartItemId, Integer userId) {
		// TODO Auto-generated method stub
		cartDao.deleteById(cartItemId);
		return cartDao.findByUserId(userId);
		
	}
}

package com.foodexpress.customer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.admin.dao.MenuItemDao;
import com.foodexpress.admin.model.RestaurantMenuItem;
import com.foodexpress.customer.dao.CartDao;
import com.foodexpress.customer.dto.UserCart;
import com.foodexpress.customer.model.Cart;

@Service
public class CartService implements ICart {

    @Autowired
    private CartDao cartDao;
    
    @Autowired
    private MenuItemDao menuItemDao;

    @Override
    public List<Cart> addToCart(Cart cart) {
        Cart existingCart = cartDao.findByUserIdAndItemId(cart.getUserId(), cart.getItemId());
        
        if (existingCart != null) {
            int stock = menuItemDao.findById(existingCart.getItemId())
                                   .orElseThrow(() -> new IllegalArgumentException("Item not found"))
                                   .getStock();
                                   
            int newQuantity = Math.min(stock, existingCart.getQuantity() + cart.getQuantity());
            existingCart.setQuantity(newQuantity);
            cartDao.save(existingCart);
        } else {
            int stock = menuItemDao.findById(cart.getItemId())
                                   .orElseThrow(() -> new IllegalArgumentException("Item not found"))
                                   .getStock();
            cart.setQuantity(Math.min(stock, cart.getQuantity()));
            cartDao.save(cart);
        }
        
        return cartDao.findByUserId(cart.getUserId());
    }


    @Override
    public List<Cart> updateCart(Cart cart, int flag) {
        // Fetch stock information from the MenuItem table
        int stock = menuItemDao.findById(cart.getItemId())
                               .orElseThrow(() -> new IllegalArgumentException("Item not found"))
                               .getStock();

        Cart existingCart = cartDao.findByUserIdAndItemId(cart.getUserId(), cart.getItemId());
        if (existingCart != null) {
            if (flag == 1) {
                // Increment quantity, but do not exceed available stock
                existingCart.setQuantity(Math.min(existingCart.getQuantity() + 1, stock));
            } else {
                // Decrement quantity, but do not go below 1
                existingCart.setQuantity(Math.max(existingCart.getQuantity() - 1, 1));
            }
            cartDao.save(existingCart);
        } else {
            // Insert a new record if no matching record exists, with stock constraint
            cart.setQuantity(Math.min(cart.getQuantity(), stock));
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


	public List<UserCart> getMyCart(int userId) {
	    List<Cart> carts = cartDao.findByUserId(userId);
	    List<UserCart> userCarts = new ArrayList<>();

	    for (Cart cart : carts) {
	        UserCart userCart = new UserCart();
	        userCart.setCartItemId(cart.getCartItemId());
	        
	        userCart.setUserId(cart.getUserId());
	        userCart.setItemId(cart.getItemId());
	        userCart.setQuantity(cart.getQuantity());

	        // Assuming Cart has a reference to the Item entity or you can fetch it using itemId
	        RestaurantMenuItem item = menuItemDao.findById(cart.getItemId()).orElse(null); // Adjust if necessary
	        if (item != null) {
	            userCart.setPrice(item.getPrice());
	            userCart.setItemName(item.getName());
	            userCart.setImage(item.getImages()); // Assuming the image field is in the Item entity
	        }

	        userCarts.add(userCart);
	    }

	    return userCarts;
	}


	
}

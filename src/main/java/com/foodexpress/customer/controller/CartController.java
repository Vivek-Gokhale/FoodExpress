package com.foodexpress.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodexpress.customer.dto.UserCart;
import com.foodexpress.customer.model.Cart;
import com.foodexpress.customer.service.CartService;

@RestController

public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add-to-cart")
    public List<Cart> addToCartHandler(@RequestBody Cart cart) {
        return cartService.addToCart(cart);
    }

    @PutMapping("/update-cart/{flag}")
    public List<Cart> updateCartHandler(@PathVariable("flag") int flag,@RequestBody Cart cart) {
        return cartService.updateCart(cart, flag);
    }

    @DeleteMapping("/remove-from-cart/{cartItemId}/{userId}")
    public List<Cart> updateCartHandler(@PathVariable("cartItemId") Integer cartItemId, @PathVariable("userId") Integer userId) {
        return cartService.removeFromCart(cartItemId, userId);
    }
    
    @GetMapping("/get-my-cart/{userId}")
    public ResponseEntity<List<UserCart>> getMyCartHandler(@PathVariable("userId") int userId) {
        List<UserCart> userCartList = cartService.getMyCart(userId);
        
        if (userCartList.isEmpty()) {
            // Return 404 NOT_FOUND if the cart is empty
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(null);
        }
        
        return ResponseEntity.ok(userCartList);
    }



    
}

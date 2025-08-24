package com.adarsh.foodiesapi.service;

import com.adarsh.foodiesapi.io.CartRequest;
import com.adarsh.foodiesapi.io.CartResponse;

public interface CartService {
    CartResponse addToCart(CartRequest request);
    CartResponse getCart();
    void clearCart();
    CartResponse removeFromCart(CartRequest request);
}

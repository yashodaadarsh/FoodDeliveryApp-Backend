package com.adarsh.foodiesapi.service;

import com.adarsh.foodiesapi.Entity.CartEntity;
import com.adarsh.foodiesapi.Repository.CartRepository;
import com.adarsh.foodiesapi.io.CartRequest;
import com.adarsh.foodiesapi.io.CartResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    @Override
    public CartResponse addToCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId();
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
        CartEntity cart = cartOptional.orElseGet( () -> new CartEntity(loggedInUserId,new HashMap<>()));
        Map<String,Integer> cartItems = cart.getItems();
        cartItems.put(request.getFoodId(),cartItems.getOrDefault(request.getFoodId(),0)+1 );
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId = userService.findByUserId();
        CartEntity entity = cartRepository.findByUserId(loggedInUserId)
                .orElse(new CartEntity(null,loggedInUserId,new HashMap<>()));
        return convertToResponse(entity);
    }

    @Override
    public void clearCart() {
        String loggedInUserId = userService.findByUserId();
        cartRepository.deleteByUserId(loggedInUserId);
    }

    @Override
    public CartResponse removeFromCart( CartRequest request ) {
        String loggedInUserId = userService.findByUserId();
        CartEntity entity = cartRepository.findByUserId(loggedInUserId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        Map<String, Integer> cartItems = entity.getItems();
        if( cartItems.containsKey(request.getFoodId()) ){
            int currentQty = cartItems.get(request.getFoodId());
            if(currentQty > 0 ){
                cartItems.put(request.getFoodId(),currentQty-1);
            }
            if( cartItems.get(request.getFoodId()) == 0 ){
                cartItems.remove(request.getFoodId());
            }
            entity = cartRepository.save(entity);
        }
        return convertToResponse(entity);
    }

    private CartResponse convertToResponse(CartEntity cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getItems())
                .build();
    }
}

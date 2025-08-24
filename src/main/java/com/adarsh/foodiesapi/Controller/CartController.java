package com.adarsh.foodiesapi.Controller;

import com.adarsh.foodiesapi.io.CartRequest;
import com.adarsh.foodiesapi.io.CartResponse;
import com.adarsh.foodiesapi.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request ){
        System.out.println("Entered ");
        String foodId = request.getFoodId();
        if( foodId == null || foodId.isEmpty() ){ // fix this for not stored foodId's in the database
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"foodId not found");
        }
        return cartService.addToCart(request);
    }

    @GetMapping
    public CartResponse getCart(){
        return cartService.getCart();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(){
        cartService.clearCart();
    }

    @PutMapping
    public CartResponse removeFromCart( @RequestBody CartRequest request ){
        String foodId = request.getFoodId();
        if( foodId == null || foodId.isEmpty() ){ // fix this for not stored foodId's in the database
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"foodId not found");
        }
        return cartService.removeFromCart(request);
    }
}

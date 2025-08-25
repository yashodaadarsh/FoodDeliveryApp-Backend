package com.adarsh.foodiesapi.Controller;

import com.adarsh.foodiesapi.io.OrderRequest;
import com.adarsh.foodiesapi.io.OrderResponse;
import com.adarsh.foodiesapi.service.OrderService;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrderWithPayment(@RequestBody OrderRequest request) throws RazorpayException {
        OrderResponse response = orderService.createOrderWithPayment(request);
        return response;
    }

    @PostMapping("/verify")
    public void verifyPayment(@RequestBody Map<String,String> paymentData ){
        orderService.verifyPayment(paymentData,"paid");
    }

    @GetMapping
    public List<OrderResponse> getOrders(){
        return orderService.getUserOrders();
    }

    @DeleteMapping("{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder( @PathVariable String orderId ){
        orderService.removeOrder(orderId);
    }

    //Admin panel
    @GetMapping("/all")
    public List<OrderResponse> getOrdersOfAllUsers(){
        return orderService.getOrdersOfAllUsers();
    }

    //Admin panel
    @PatchMapping("/status/{orderId}")
    public void updateOrderStatus( @PathVariable String orderId , @RequestParam String status ){
        orderService.updateOrderStatus(orderId,status);
    }

}

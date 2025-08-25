package com.adarsh.foodiesapi.service;

import com.adarsh.foodiesapi.Entity.OrderEntity;
import com.adarsh.foodiesapi.Repository.CartRepository;
import com.adarsh.foodiesapi.Repository.OrderRepository;
import com.adarsh.foodiesapi.io.OrderRequest;
import com.adarsh.foodiesapi.io.OrderResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplementation implements OrderService{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;

    @Value("${razorpay_key}")
    private String RAZORPAY_KEY;
    @Value("${razorpay_secret}")
    private String RAZORPAY_SECRET;

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {
        OrderEntity newOrder = convertToEntity(request);
        newOrder = orderRepository.save(newOrder); // contains the mongodb id

        //create razorpay payment order
        RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY,RAZORPAY_SECRET);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",newOrder.getAmount());
        orderRequest.put("currency" , "INR");
        orderRequest.put("payment_capture",1);
        Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        String razorpayOrderId = razorpayOrder.get("id");

        newOrder.setRazorpayOrderId(razorpayOrderId);
        String loggedInUserId = userService.findByUserId();
        newOrder.setUserId(loggedInUserId);
        newOrder = orderRepository.save(newOrder);

        return convertToResponse(newOrder);
    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {
        String razorpayOrderId = paymentData.get("razorpay_order_id");
        OrderEntity existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Order not Found"));
        existingOrder.setPaymentStatus(status);
        existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));
        orderRepository.save(existingOrder);

        //payment is done. Now we need to delete the items in the cart
        if(status.equalsIgnoreCase("paid")){
            cartRepository.deleteByUserId(existingOrder.getUserId());
        }


    }

    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedInUserId = userService.findByUserId();
        List<OrderEntity> ordersList = orderRepository.findByUserId(loggedInUserId);
        return ordersList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {
        List<OrderEntity> allOrders = orderRepository.findAll();
        return allOrders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        OrderEntity entity = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("Order not found"));
        entity.setOrderStatus(status);
        orderRepository.save(entity);
    }

    private OrderResponse convertToResponse(OrderEntity newOrder) {
        return OrderResponse.builder()
                .id(newOrder.getId())
                .amount(newOrder.getAmount())
                .userAddress(newOrder.getUserAddress())
                .userId(newOrder.getUserId())
                .razorpayOrderId(newOrder.getRazorpayOrderId())
                .paymentStatus(newOrder.getPaymentStatus())
                .orderStatus(newOrder.getOrderStatus())
                .email(newOrder.getEmail())
                .phoneNumber(newOrder.getPhoneNumber())
                .orderItems(newOrder.getOrderItems())
                .build();
    }

    private OrderEntity convertToEntity(OrderRequest request) {
        return OrderEntity.builder()
                .userAddress(request.getUserAddress())
                .orderItems(request.getOrderItems())
                .amount(request.getAmount())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .orderStatus(request.getOrderStatus())
                .build();
    }
}

package com.adarsh.foodiesapi.service;

import com.adarsh.foodiesapi.io.UserRequest;
import com.adarsh.foodiesapi.io.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest request);
    String findByUserId();
}

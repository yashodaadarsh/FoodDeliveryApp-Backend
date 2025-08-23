package com.adarsh.foodiesapi.Controller;

import com.adarsh.foodiesapi.io.UserRequest;
import com.adarsh.foodiesapi.io.UserResponse;
import com.adarsh.foodiesapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody  UserRequest request){
        UserResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
}

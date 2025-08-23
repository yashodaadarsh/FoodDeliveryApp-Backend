package com.adarsh.foodiesapi.service;

import com.adarsh.foodiesapi.Entity.UserEntity;
import com.adarsh.foodiesapi.Repository.UserRepository;
import com.adarsh.foodiesapi.io.UserRequest;
import com.adarsh.foodiesapi.io.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private AuthenticationFacade authenticationFacade;

    @Override
    public UserResponse registerUser(UserRequest request) {
        UserEntity newUser = convertToEntity( request );
        newUser = repository.save(newUser);
        return convertToResponse(newUser);
    }

    @Override
    public String findByUserId() {
        String loggedInUserEmail = authenticationFacade.authentication().getName();
        UserEntity loggedInUser = repository.findByEmail(loggedInUserEmail).orElseThrow( () -> new UsernameNotFoundException("User not Found"));
        return loggedInUser.getId();
    }

    private UserEntity convertToEntity(UserRequest request){
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .build();
    }

    private UserResponse convertToResponse( UserEntity userEntity ){
        return UserResponse.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .build();
    }
}

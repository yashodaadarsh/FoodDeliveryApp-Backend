package com.adarsh.foodiesapi.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticaionFacadeImplementation implements AuthenticationFacade {
    @Override
    public Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

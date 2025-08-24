package com.adarsh.foodiesapi.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

public interface AuthenticationFacade {
    Authentication authentication();
}

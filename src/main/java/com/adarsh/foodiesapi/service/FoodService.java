package com.adarsh.foodiesapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FoodService {
    String uploadFile(MultipartFile file);
}

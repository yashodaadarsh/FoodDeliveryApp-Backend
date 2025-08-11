package com.adarsh.foodiesapi.service;

import com.adarsh.foodiesapi.io.FoodRequest;
import com.adarsh.foodiesapi.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FoodService {

    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest request , MultipartFile file );
    List<FoodResponse> readFoods();
    FoodResponse readFood( String id );
    boolean deleteFile(String publicId);
    void deleteFood(String id);

}

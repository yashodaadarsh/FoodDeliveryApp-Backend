package com.adarsh.foodiesapi.Controller;

import com.adarsh.foodiesapi.io.FoodRequest;
import com.adarsh.foodiesapi.io.FoodResponse;
import com.adarsh.foodiesapi.service.FoodService;
import com.adarsh.foodiesapi.service.FoodServiceImplementation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
public class FoodController {


    private FoodService foodService;


    @PostMapping
    public FoodResponse addFood( @RequestPart("food") String foodString , @RequestPart("image") MultipartFile file ){
        ObjectMapper mapper = new ObjectMapper();
        FoodRequest request = null;
        try{
            request = mapper.readValue(foodString,FoodRequest.class);

        }
        catch ( JsonProcessingException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid JSON Format");
        }
        FoodResponse response = foodService.addFood(request,file);
        return response;
    }

    @GetMapping
    public List<FoodResponse> readFoods(){
        return foodService.readFoods();
    }
}

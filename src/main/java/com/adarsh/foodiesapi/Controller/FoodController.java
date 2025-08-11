package com.adarsh.foodiesapi.Controller;

import com.adarsh.foodiesapi.service.FoodServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class FoodController {
    @Autowired
    private FoodServiceImplementation foodServiceImplementation;
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("image") MultipartFile file){
        return foodServiceImplementation.uploadFile(file);
    }
}

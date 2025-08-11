package com.adarsh.foodiesapi.service;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FoodServiceImplementation implements FoodService{

    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String key = UUID.randomUUID().toString();
        try {
            System.out.println("ready for upload");
            Map upload = cloudinary.uploader().upload(file.getBytes(), Map.of(
                    "resource_type", "auto",
                    "public_id", key
            ));
            System.out.println("uploaded");
            String url = upload.get("url").toString();
            if( url != null ){
                return url;
            }
            else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File upload failed");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occured while uploading the file");
        }
    }
}

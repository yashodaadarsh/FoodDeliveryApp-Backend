package com.adarsh.foodiesapi.service;

import com.adarsh.foodiesapi.Entity.FoodEntity;
import com.adarsh.foodiesapi.Repository.FoodRepository;
import com.adarsh.foodiesapi.io.FoodRequest;
import com.adarsh.foodiesapi.io.FoodResponse;
import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FoodServiceImplementation implements FoodService{

    private Cloudinary cloudinary;
    private FoodRepository foodRepository;

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

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        FoodEntity newFoodEntity = convertToEntity(request);
        String imageUrl = uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);
        FoodEntity save = foodRepository.save(newFoodEntity);
        return convertToResponse(save);
    }

    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> databaseEntries = foodRepository.findAll();
        return databaseEntries.stream().map( object -> convertToResponse(object) ).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood( String id ) {
        FoodEntity existingFood = foodRepository.findById( id ).orElseThrow( () -> new RuntimeException("Food not found for the id: " + id ) );
        return convertToResponse(existingFood);
    }

    private String extractPublicIdFromUrl(String url) {
        String path = url.substring(url.indexOf("/upload/") + 8);
        // Remove version if present
        if (path.startsWith("v") && path.length() > 2 && Character.isDigit(path.charAt(1))) {
            path = path.substring(path.indexOf("/") + 1);
        }
        // Remove extension
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex != -1) {
            path = path.substring(0, dotIndex);
        }
        return path;
    }


    @Override
    public boolean deleteFile(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, Map.of(
                    "resource_type", "image" // auto detects image/video/raw
            ));

            // Cloudinary returns "result" : "ok" if deletion is successful
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while deleting the file");
        }
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response = readFood(id);
        String imageUrl = response.getImageUrl();
        String publicId = extractPublicIdFromUrl(imageUrl);
        boolean isFileDeleted = deleteFile(publicId);
        if( isFileDeleted ){
            //System.out.println("Deleted the file from cloudinary");
            foodRepository.deleteById( id );
        }
    }

    private FoodEntity convertToEntity( FoodRequest request ){
        return FoodEntity
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();
    }

    private FoodResponse convertToResponse( FoodEntity entity ){
        return FoodResponse
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}

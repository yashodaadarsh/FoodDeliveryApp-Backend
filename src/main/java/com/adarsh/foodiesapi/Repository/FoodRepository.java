package com.adarsh.foodiesapi.Repository;

import com.adarsh.foodiesapi.Entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepository extends MongoRepository<FoodEntity,String> {
}

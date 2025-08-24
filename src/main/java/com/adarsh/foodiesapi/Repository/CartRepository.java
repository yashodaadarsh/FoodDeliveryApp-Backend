package com.adarsh.foodiesapi.Repository;

import com.adarsh.foodiesapi.Entity.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<CartEntity,Integer> {

    Optional<CartEntity> findByUserId(String userId);

    void deleteByUserId(String userId);
}

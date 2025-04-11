package com.humber.foodshare.repositories;

import com.humber.foodshare.models.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends MongoRepository<FoodItem, String> {
    List<FoodItem> findByIsWanted(boolean isWanted);
}
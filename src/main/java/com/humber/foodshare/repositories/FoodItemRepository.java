package com.humber.foodshare.repositories;

import com.humber.foodshare.models.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//food item repository
// Marks this as a Spring Data repository
@Repository
public interface FoodItemRepository extends MongoRepository<FoodItem, String> {
}

package com.humber.foodshare.repositories;

import com.humber.foodshare.models.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends MongoRepository<FoodItem, String>, PagingAndSortingRepository<FoodItem, String> {
    List<FoodItem> findByIsWanted(boolean isWanted);
}
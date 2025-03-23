package com.humber.foodshare.services;

import com.humber.foodshare.models.FoodItem;
import com.humber.foodshare.repositories.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {
    private final FoodItemRepository foodItemRepository;

    @Autowired
    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public List<FoodItem> findAll() {
        return foodItemRepository.findAll();
    }

    public FoodItem save(FoodItem foodItem) {
        System.out.println("Saving food item: " + foodItem);

        if (foodItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        return foodItemRepository.save(foodItem);
    }

    public FoodItem findById(String id) {
        // research id
        return foodItemRepository.findById(id).orElse(null);
    }
}

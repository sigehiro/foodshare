package com.humber.foodshare.services;

import com.humber.foodshare.models.FoodItem;
import com.humber.foodshare.repositories.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Using MongoDB for pagination
    public Map<String, Object> findAll(int pageNo, int pageSize) {
        // Validate page number and size
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        // MongoDB用のメソッド
        List<FoodItem> foodItems = foodItemRepository.findAll(pageable).getContent();

        long totalItems = foodItemRepository.count();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        // Create a response map
        Map<String, Object> response = new HashMap<>();
        response.put("foodItems", foodItems);
        response.put("currentPage", pageNo);
        response.put("totalPages", totalPages);
        response.put("totalItems", totalItems);

        return response;
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

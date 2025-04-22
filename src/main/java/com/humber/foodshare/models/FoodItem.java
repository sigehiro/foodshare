package com.humber.foodshare.models;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "foodItems") // MongoDB:collection name
public class FoodItem {
    @Id
    private String id;
    private String foodType;
    private int quantity;
    private String allergenInfo;
    private LocalDateTime pickupTime;
    private String location;
    private boolean isWanted;
    private String imageUrl; // New field for image storage
    private String imageType;
    @Lob
    private byte[] imageData;
}
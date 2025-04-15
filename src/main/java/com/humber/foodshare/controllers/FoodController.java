package com.humber.foodshare.controllers;

import com.humber.foodshare.models.FoodItem;
import com.humber.foodshare.repositories.FoodItemRepository;
import com.humber.foodshare.services.FoodItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/foodshare")
@SessionAttributes("wantedItems")
public class FoodController {

    private final FoodItemService foodItemService;
    private final FoodItemRepository foodItemRepository;

    @Autowired
    public FoodController(FoodItemService foodItemService,
                          FoodItemRepository foodItemRepository) {
        this.foodItemService = foodItemService;
        this.foodItemRepository = foodItemRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to FoodShare");
        return "home";
    }

    @GetMapping("/user-dashboard")
    public String userDashboard(Model model, HttpSession session) {
        List<FoodItem> wantedItems = (List<FoodItem>) session.getAttribute("wantedItems");
        model.addAttribute("wantedItems", wantedItems != null ? wantedItems : new ArrayList<>());
        return "user_dashboard";
    }

    @GetMapping("/food-listing")
    public String foodListing(Model model) {
        // Using MongoDB repository directly
        List<FoodItem> availableItems = foodItemRepository.findByIsWanted(false);
        model.addAttribute("foodItems", availableItems);
        model.addAttribute("currentPage", 1);
        return "food_listing";
    }

    @GetMapping("/food-posting")
    public String foodPosting(Model model) {
        model.addAttribute("foodItem", new FoodItem());
        return "food_posting";
    }

    @PostMapping("/save")
    public String saveFoodItem(@ModelAttribute FoodItem foodItem,
                               @RequestParam(value = "foodImage", required = false) MultipartFile file,
                               RedirectAttributes redirectAttributes) throws IOException {

        // Handle image upload if present
        if (file != null && !file.isEmpty()) {
            try {
                // Validate file type
                String contentType = file.getContentType();
                if (contentType == null ||
                        (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                    redirectAttributes.addFlashAttribute("error", "Only JPEG or PNG images are allowed");
                    return "redirect:/foodshare/food-posting";
                }

                // Validate file size (5MB max)
                if (file.getSize() > 5 * 1024 * 1024) {
                    redirectAttributes.addFlashAttribute("error", "File size exceeds 5MB limit");
                    return "redirect:/foodshare/food-posting";
                }

                // Save file to uploads directory
//                Path uploadPath = Paths.get("uploads");
                String projectDirectory = System.getProperty("user.dir");
                Path uploadPath = Paths.get(projectDirectory, "src", "main", "resources", "static", "uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Set image path on food item
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename != null ?
                        originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String fileName = UUID.randomUUID().toString() + fileExtension;

                Files.copy(file.getInputStream(), uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);
                foodItem.setImageUrl("/uploads/" + fileName);

                // Set image data (byte array) on food item
                foodItem.setImageData(file.getBytes());
                foodItem.setImageType(contentType);

            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload image");
                return "redirect:/foodshare/food-posting";
            }
        }

        // Save using MongoDB repository
        foodItemRepository.save(foodItem);
        redirectAttributes.addFlashAttribute("success", "Food posted successfully!");

        return "redirect:/foodshare/food-listing"; // redirect to food listing page(302確認済み)
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        return "terms";
    }

    @GetMapping("/policy")
    public String policy(Model model) {
        return "policy";
    }

    @PostMapping("/want-food")
    public String wantFoodItem(@RequestParam String id,
                               Model model,
                               HttpSession session) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid food item id: " + id));

        // update item condition
        foodItem.setWanted(true);
        foodItemRepository.save(foodItem);

        // add wantedItems
        List<FoodItem> wantedItems = (List<FoodItem>) session.getAttribute("wantedItems");
        if (wantedItems == null) {
            wantedItems = new ArrayList<>();
            session.setAttribute("wantedItems", wantedItems);
        }

        wantedItems.add(foodItem);

        return "redirect:/foodshare/user-dashboard";
    }
}
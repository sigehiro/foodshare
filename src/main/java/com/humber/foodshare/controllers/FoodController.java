package com.humber.foodshare.controllers;

import com.humber.foodshare.models.FoodItem;
import com.humber.foodshare.models.User;
import com.humber.foodshare.repositories.FoodItemRepository;
import com.humber.foodshare.repositories.UserRepository;
import com.humber.foodshare.services.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Controller
@RequestMapping("/foodshare")
@SessionAttributes("wantedItems")
public class FoodController {

    private final FoodItemService foodItemService;
    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public FoodController(FoodItemService foodItemService,
                          FoodItemRepository foodItemRepository,
                          UserRepository userRepository) {
        this.foodItemService = foodItemService;
        this.foodItemRepository = foodItemRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername(); // usually your user's email
        } else if (authentication != null) {
            email = authentication.getName();
        }

        if (email != null) {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            optionalUser.ifPresent(user -> model.addAttribute("user", user));
        }
        model.addAttribute("message", "Welcome to FoodShare");
        return "home";


    }

    @GetMapping("/user-dashboard")
    public String userDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // ここでusername == loadUserByUsernameからemail 取得してきている
            email = userDetails.getUsername();
        } else if (authentication != null) {
            email = authentication.getName();
        }

        if (email == null) {
            model.addAttribute("wantedItems", new ArrayList<FoodItem>());
            return "user_dashboard";
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            model.addAttribute("wantedItems", new ArrayList<FoodItem>());
            return "user_dashboard";
        }

        User user = optionalUser.get();
        //for user is used by user-dashboard
        model.addAttribute("user", user);

        List<String> wantedIds = user.getWantedFoodItemIds();
        List<FoodItem> wantedItems = (wantedIds == null || wantedIds.isEmpty()) ?
                new ArrayList<>() :
                foodItemRepository.findAllById(wantedIds);

        model.addAttribute("wantedItems", wantedItems);
        return "user_dashboard";
    }


    // Using FoodItemService to get paginated results in MongoDB
    @GetMapping("/food-listing")
    public String foodListing(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 12; // page size set 12
        // Get food items from the service
        Map<String, Object> result = foodItemService.findAll(page, pageSize);

        model.addAttribute("foodItems", result.get("foodItems"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalItems", result.get("totalItems"));

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
                // Path uploadPath = Paths.get("uploads");
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
    public String wantFoodItem(@RequestParam String id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername();
        } else if (authentication != null) {
            email = authentication.getName();
        }

        if (email == null) {
            throw new IllegalArgumentException("User is not authenticated");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + email);
        }
        User user = optionalUser.get();

        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid food item id: " + id));

//        foodItem.setWanted(true);
//        foodItemRepository.save(foodItem);

        // 数量チェック
        int quantity = foodItem.getQuantity();

        if (quantity <= 1) {
            // 数量が1以下なら削除
            foodItemRepository.delete(foodItem);
        } else {
            // 数量が2以上なら1減らして保存
            foodItem.setQuantity(quantity - 1);
            foodItemRepository.save(foodItem);
        }

        if (user.getWantedFoodItemIds() == null) {
            user.setWantedFoodItemIds(new ArrayList<>());
        }
        if (!user.getWantedFoodItemIds().contains(id)) {
            user.getWantedFoodItemIds().add(id);
            userRepository.save(user);
        }

        return "redirect:/foodshare/user-dashboard";
    }
}
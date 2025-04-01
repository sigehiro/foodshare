package com.humber.foodshare.controllers;


import com.humber.foodshare.models.FoodItem;
import com.humber.foodshare.services.FoodItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/foodshare")
@SessionAttributes("wantedItems")
public class FoodController {

    private final FoodItemService foodItemService;


    @Autowired
    public FoodController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }


    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to FoodShare");
        return "home";
    }


    @GetMapping("/user-dashboard")
    public String userDashboard(Model model, HttpSession session) {
        // ユーザーが認証されているか確認
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() ||
//                !(authentication instanceof UsernamePasswordAuthenticationToken)) {
//            return "redirect:/login?error=true"; // 未認証の場合はログイン画面にリダイレクト
//        }

        //past orders data
//        List wantedItems = (List) session.getAttribute("wantedItems");
//        model.addAttribute("wantedItems", wantedItems != null ? wantedItems : new ArrayList<>());

        List<FoodItem> wantedItems = (List<FoodItem>) session.getAttribute("wantedItems");
        model.addAttribute("wantedItems", wantedItems != null ? wantedItems : new ArrayList<>());

        return "user_dashboard";
    }


    @GetMapping("/food-listing")
    public String foodListing(Model model) {
        //isWanted = false -> show the food items that are available
        List<FoodItem> foodItems = foodItemService.findAll();
        List<FoodItem> availableItems = new ArrayList<>();

        for (FoodItem item : foodItems) {
            if (!item.isWanted()) {
                availableItems.add(item);
            }
        }

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
    public String saveFoodItem(@ModelAttribute FoodItem foodItem) {
        foodItemService.save(foodItem);
        return "redirect:/foodshare/food-listing";
    }

    //#TODO 暫定でOPenにする。SecurityConfigのコードをCloseにしたらここは、コメントアウトにする
    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @GetMapping("/sign-in")
    public String login(Model model) {
        return "sign_in";
    }
    //TODO ここまで

    @GetMapping("/admin-dashboard")
    public String adminBoard(Model model) {
        return "admin_dashboard";
    }


    @PostMapping("/want-food")
    public String wantFoodItem(@RequestParam String id,
                               Model model,
                               HttpSession session) {
        FoodItem foodItem = foodItemService.findById(id);

        // update item condition. set wanted to true
        foodItem.setWanted(true);
        foodItemService.save(foodItem); // save th MongoDB

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

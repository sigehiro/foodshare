package com.humber.foodshare.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foodshare")
public class FoodController {


    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to FoodShare");
        return "home";
    }

    @GetMapping("/user-dashboard")
    public String userDashboard(Model model) {
        return "user_dashboard";
    }

    @GetMapping("/food-listing")
    public String foodListing(Model model) {
        return "food_listing";
    }

    @GetMapping("/food-posting")
    public String foodPosting(Model model) {
        return "food_posting";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @GetMapping("/sign-in")
    public String login(Model model) {
        return "sign_in";
    }

    @GetMapping("/admin-dashboard")
    public String adminBoard(Model model) {
        return "admin_dashboard";
    }

}

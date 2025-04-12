package com.humber.foodshare.controllers;

import com.humber.foodshare.models.User;
import com.humber.foodshare.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController implements ErrorController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/error")
    public String error403() {
        return "auth/error403"; // move to erroer page
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("user", new User());
        model.addAttribute("message", message);
        return "auth/register";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        int statusCode = userService.saveUser(user);
        if (statusCode == 0) {
            System.out.println("User already exists: " + user.getEmail());
            return "redirect:/register?message=User already exists"; // ユーザーが既に存在する場合
        } else {
            System.out.println("User registered successfully: " + user.getEmail());
            return "redirect:/login?message=User registered successfully"; // successful registration: move to login page
        }
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("user", new User());
        model.addAttribute("message", message);
        return "auth/login";
    }

}
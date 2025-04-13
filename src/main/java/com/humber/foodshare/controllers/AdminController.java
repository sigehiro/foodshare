package com.humber.foodshare.controllers;

import com.humber.foodshare.models.User;
import com.humber.foodshare.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/foodshare")
public class AdminController implements ErrorController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin_dashboard"; // Create an admin_dashboard.html template
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam String email) {
        userService.deleteUserByEmail(email);
        return "redirect:/foodshare/admin-dashboard";
    }

    @PostMapping("/admin/freeze")
    public String freezeUser(@RequestParam String email) {
        userService.freezeUserByEmail(email);
        return "redirect:/foodshare/admin-dashboard";
    }
}

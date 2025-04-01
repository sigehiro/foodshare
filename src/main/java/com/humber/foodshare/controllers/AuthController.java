//package com.humber.foodshare.controllers;
//
//import com.humber.foodshare.models.User;
////import com.humber.foodshare.services.UserService;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class AuthController implements ErrorController {
//
//    private final UserService userService;
//
//    public AuthController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/error")
//    public String error403() {
//        return "auth/error403"; // エラーページのテンプレート名
//    }
//
//
//    @GetMapping("/register")
//    public String showRegistrationForm(Model model, @RequestParam(required = false) String message) {
//        model.addAttribute("user", new User());
//        model.addAttribute("message", message); // メッセージをモデルに追加
//        return "auth/register";
//    }
//
//
//    @PostMapping("/register")
//    public String register(@ModelAttribute User user) {
//        int statusCode = userService.saveUser(user);
//        if (statusCode == 0) {
//            return "redirect:/register?message=User already exists"; // ユーザーが既に存在する場合
//        } else {
//            return "redirect:/login?message=User registered successfully"; // 成功した場合
//        }
//    }
//
//    @GetMapping("/login")
//    public String login(Model model, @RequestParam(required = false) String message) {
//        model.addAttribute("user", new User());
//        model.addAttribute("message", message);
//        return "auth/login";
//    }
//
//    //#TODO 暫定でOPenにする。SecurityConfigのコードをCloseにしたらここは、コメントアウトにする
//    @PostMapping("/login")
//    public String login(@ModelAttribute User user, Model model) {
//        model.addAttribute("message", "Login successful");
//        System.out.println("Attempting to log in with username: " + user.getUsername());
//        return "redirect:/foodshare/user-dashboard";
//    }
//}
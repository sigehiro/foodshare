package com.humber.foodshare.controllers;

import com.humber.foodshare.models.Message;
import com.humber.foodshare.models.User;
import com.humber.foodshare.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import com.humber.foodshare.repositories.MessageRepository;
@Controller
@RequestMapping("/foodshare")
public class AdminController implements ErrorController {

    private final UserService userService;
    // injecting message repository
    private final MessageRepository messageRepository;
    public AdminController(UserService userService, MessageRepository messageRepository) {
        this.userService = userService;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userService.findAllUsers();
        List<Message> messages = messageRepository.findAllByOrderByTimestampDesc();
        System.out.println("Messages in DB: " + messages.size());
        messages.forEach(System.out::println);
        model.addAttribute("users", users);
        model.addAttribute("messages", messages);
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
    //post mapping to recieve admin reply
    @PostMapping("/admin/reply")
    public String replyToMessage(@RequestParam String messageId, @RequestParam String reply) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setReply(reply);
        messageRepository.save(message);
        return "redirect:/foodshare/admin-dashboard";
    }



}

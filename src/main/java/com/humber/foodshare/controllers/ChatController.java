package com.humber.foodshare.controllers;

import com.humber.foodshare.models.Message;
import com.humber.foodshare.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

//chatcontroller
@RestController
//endpoint api for chat
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private MessageRepository messageRepo;
       //postmapping for savemessage endpoint
    @PostMapping
    public Message saveMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        System.out.println("🚨 Saving message: " + message); // Confirm message received
        return messageRepo.save(message);
    }

  //get mapping to list and get all messages
    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepo.findAllByOrderByTimestampDesc();
    }
}

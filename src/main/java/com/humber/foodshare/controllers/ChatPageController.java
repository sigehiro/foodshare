package com.humber.foodshare.controllers;

import com.humber.foodshare.models.ChatMessage;

import com.humber.foodshare.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;


public class  ChatPageController{

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message) {
        return message;
    }




}


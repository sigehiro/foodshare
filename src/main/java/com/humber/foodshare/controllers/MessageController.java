package com.humber.foodshare.controllers;

import com.humber.foodshare.models.Message;
import com.humber.foodshare.services.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Handle initial page load and chat session setup
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        String sessionId = initializeMessageSession(session);
        model.addAttribute("messageHistory", messageService.getMessageHistory(sessionId));
        return "home";
    }

    // WebSocket endpoint for sending messages
    @MessageMapping("/message/{sessionId}")
    @SendTo("/topic/messages/{sessionId}")
    public Message handleMessage(
            @DestinationVariable String sessionId,
            Message message) {

        message.setSessionId(sessionId);
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageService.saveMessage(message);

        // You could add additional processing here
        if (message.getContent().contains("help")) {
            sendAutoResponse(sessionId, "For assistance, please contact support@foodshare.com");
        }

        return savedMessage;
    }

    private String initializeMessageSession(HttpSession session) {
        String sessionId = (String) session.getAttribute("messageSessionId");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("messageSessionId", sessionId);

            Message welcomeMsg = new Message();
            welcomeMsg.setSenderId("system");
            welcomeMsg.setContent("Welcome to FoodShare Connect! How can we help you?");
            welcomeMsg.setSessionId(sessionId);
            messageService.saveMessage(welcomeMsg);
        }
        return sessionId;
    }

    private void sendAutoResponse(String sessionId, String response) {
        Message autoReply = new Message();
        autoReply.setSenderId("system");
        autoReply.setContent(response);
        autoReply.setSessionId(sessionId);

        messageService.saveMessage(autoReply);
        messagingTemplate.convertAndSend("/topic/messages/" + sessionId, autoReply);
    }
}
//package com.humber.foodshare.services;
//
//import com.humber.foodshare.models.ChatMessage;
//import com.humber.foodshare.repositories.ChatMessageRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.time.LocalDateTime;
//
//@Service
//public class ChatService {
//
//    private final ChatMessageRepository chatMessageRepository;
//
//    @Autowired
//    public ChatService(ChatMessageRepository chatMessageRepository) {
//        this.chatMessageRepository = chatMessageRepository;
//    }
//
//    public void saveMessage() {
//        ChatMessage msg = new ChatMessage(
//                null,
//                "Shevonne",
//                "Hey! Is this still available?",
//                "Admin",
//                LocalDateTime.now(),
//                false
//        );
//        chatMessageRepository.save(msg);
//    }
//
//    @Autowired
//    private ChatService chatService;
//
//    @PostMapping("/send")
//    public ResponseEntity<String> sendMessage() {
//        chatService.saveMessage();
//        return ResponseEntity.ok("Message sent!");
//    }

    // You can add more methods like:
    // - getMessages()
    // - getMessagesBySender(String sender)
//    // - markAsResponded(String messageId)
//}

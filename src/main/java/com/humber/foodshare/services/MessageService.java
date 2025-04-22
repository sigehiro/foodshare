package com.humber.foodshare.services;

import com.humber.foodshare.models.Message;
import com.humber.foodshare.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessageHistory(String sessionId) {
        return messageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
    }

    public List<Message> getRecentMessages(int count) {
        // Create PageRequest with the desired count
        PageRequest pageable = PageRequest.of(0, count);
        return messageRepository.findByOrderByTimestampDesc(pageable);
    }
}
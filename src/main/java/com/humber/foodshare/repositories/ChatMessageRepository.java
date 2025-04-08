package com.humber.foodshare.repositories;

import com.humber.foodshare.models.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}

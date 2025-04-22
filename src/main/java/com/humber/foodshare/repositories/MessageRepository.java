package com.humber.foodshare.repositories;

import com.humber.foodshare.models.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySessionIdOrderByTimestampAsc(String sessionId);

    // Updated method with Pageable parameter
    List<Message> findByOrderByTimestampDesc(Pageable pageable);
}
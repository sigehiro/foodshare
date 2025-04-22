package com.humber.foodshare.repositories;

import com.humber.foodshare.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
//message repository will list messages in specific order
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findAllByOrderByTimestampDesc();
}

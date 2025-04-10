package com.humber.foodshare.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ChatMessages") // MongoDB collection name
@Entity
@Data // Includes getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    private Long id;

    private String sender;
    private String content;
    private String receiver; // for private messaging
    private LocalDateTime timestamp;
    private boolean isResponded;

}

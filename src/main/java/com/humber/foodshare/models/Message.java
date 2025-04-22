package com.humber.foodshare.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//message model

@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // No-arg constructor
@AllArgsConstructor // All-args constructor
@Document(collection = "messages")
public class Message {

    @Id
    private String id; // id field for messages
    private String sender; //sender field
    private String content; //content field
    private LocalDateTime timestamp; //timestamp field
    private String reply; //reply field
    private String username;//username field
    private String email; //email field

}
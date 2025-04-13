package com.humber.foodshare.services;

import com.humber.foodshare.models.User;
import com.humber.foodshare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // save user
    public int saveUser(User user) {
        System.out.println("Attempting to save user: " + user);
        // check exit user
        if (userRepository.findByEmail(user.getEmail()) != null) {
            System.out.println("User already exists in database: " + user.getEmail());
            return 0; // already user
        }

        // save encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getUserType() == null || user.getUserType().isEmpty()) {
            user.setUserType("RECIPIENT"); // Default role is RECIPIENT
        } else if (user.getUserType().equals("DONOR")) {
            user.setUserType("DONOR");
        }

        user.setActive(true);
        userRepository.save(user);
        System.out.println("User saved successfully: " + user.getEmail());
        return 1; // success
    }

    //find user by email for admin
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    //delete user by id for admin
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new IllegalArgumentException("No user found with email: " + email);
        }
    }

    // freeze user by id for admin
    public void freezeUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setActive(false);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("No user found with email: " + email);
        }
    }

}

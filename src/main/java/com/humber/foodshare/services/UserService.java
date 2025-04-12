package com.humber.foodshare.services;

import com.humber.foodshare.models.User;
import com.humber.foodshare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザーを保存するメソッド
    public int saveUser(User user) {
        System.out.println("Attempting to save user: " + user);
        // check exit user
        if (userRepository.findByEmail(user.getEmail()) != null) {
            System.out.println("User already exists in database: " + user.getEmail());
            return 0; // already user
        }

        // save encording passward
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getUserType() == null || user.getUserType().isEmpty()) {
            user.setUserType("RECIPIENT"); // デフォルトの役割をRECIPIENTに設定
        }else if (user.getUserType().equals("DONOR")) {
            user.setUserType("DONOR");
        }

        userRepository.save(user);
        System.out.println("User saved successfully: " + user.getEmail());
        return 1; // success
    }

    // TODO　不要かも。ユーザー名からユーザーを検索するメソッド
//    public Optional<User> findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }

}

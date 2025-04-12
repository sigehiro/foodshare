package com.humber.foodshare.services;

import com.humber.foodshare.models.User;
import com.humber.foodshare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザーを保存するメソッド
    public int saveUser(User user) {
        // ユーザーが既に存在するか確認
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return 0; // ユーザーは既に存在
        }

        // パスワードをエンコードして保存
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ユーザータイプが設定されていない場合、デフォルトを設定
        if (user.getUserType() == null || user.getUserType().isEmpty()) {
            user.setUserType("RECIPIENT"); // デフォルトの役割をRECIPIENTに設定
        }else if (user.getUserType().equals("DONOR")) {
            user.setUserType("DONOR");
        }

        userRepository.save(user);
        return 1; // ユーザーが正常に保存された
    }

    // TODO　不要かも。ユーザー名からユーザーを検索するメソッド
//    public Optional<User> findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }

}

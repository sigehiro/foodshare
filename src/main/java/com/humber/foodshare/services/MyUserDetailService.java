package com.humber.foodshare.services;

import com.humber.foodshare.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Note: Although the method name is loadUserByUsername, we are using email for lookup
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<com.humber.foodshare.models.User> userOp = Optional.ofNullable(userRepository.findByEmail(email));
        System.out.println("userOp: " + userOp);

        if (userOp.isPresent()) {
            com.humber.foodshare.models.User user = userOp.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword()) // DBから取得したパスワード
                    .roles(user.getUserType()) // userTypeをそのまま渡す
                    .build();
        } else {
            throw new UsernameNotFoundException("Email not found: " + email);
        }
    }
}

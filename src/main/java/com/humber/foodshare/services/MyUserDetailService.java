package com.humber.foodshare.services;

import com.humber.foodshare.models.User;
import com.humber.foodshare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;


@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(MyUserDetailService.class.getName());



    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to load user with username: " + username);
        Optional<User> userOp = userRepository.findByUsername(username);
        //デバック userOpがusernameを格納しているか
        System.out.println("userOp: " + userOp);

        if (userOp.isPresent()) {
            User user = userOp.get();
            logger.info("User found: " + user.getUsername());
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getUserType()) // (DBから取得したユーザータイプ) get the user type from the database
                    .build();
        } else {
            logger.warning("Username not found: " + username);
            throw new UsernameNotFoundException("Username not found" + username);
        }
    }
}

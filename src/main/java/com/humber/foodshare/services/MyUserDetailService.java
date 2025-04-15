package com.humber.foodshare.services;

import com.humber.foodshare.exceptions.AccountFrozenException;
import com.humber.foodshare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Note: Although the method name is loadUserByUsername,
    // we are using email for lookup
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to load user by email: " + email);
        Optional<com.humber.foodshare.models.User> userOp = Optional.ofNullable(userRepository.findByEmail(email));
        System.out.println("User found in database: " + userOp.isPresent());

        if (userOp.isPresent()) {
            com.humber.foodshare.models.User user = userOp.get();

            // freeze account check
            if (!user.isActive()) {
                throw new AccountFrozenException("Account is frozen: " + email);
            }

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getUserType())
                    .build();
        } else {
            throw new UsernameNotFoundException("Email not found: " + email);
        }
    }
}

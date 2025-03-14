package com.shopapi.shop.impl;

import com.shopapi.shop.models.User;
import com.shopapi.shop.models.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceImpl userService;

    public UserDetailsServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return new UserPrincipal(user);
    }

    public UserPrincipal loadUserById(UUID uuid) {
        User user = userService.getById(uuid);
        return new UserPrincipal(user);
    }
//    public UserPrincipal loadUserByEmail(String email) {
//        User user = userRepository.findUserByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new UserPrincipal(user);
//    }
//
//    public UserPrincipal loadUserByPhoneNumber(String phoneNumber) {
//        User user = userRepository.findUserByPhoneNumber(phoneNumber);
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new UserPrincipal(user);

    public User loadUserByIdentifier(String identifier) {
        return userService.getUserByIdentifier(identifier);
    }
}

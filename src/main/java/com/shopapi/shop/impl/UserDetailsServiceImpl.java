package com.shopapi.shop.impl;

import com.shopapi.shop.models.User;
import com.shopapi.shop.models.UserPrincipal;
import com.shopapi.shop.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserPrincipal(user);
    }

    public UserPrincipal loadUserById(UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
        System.out.println("Зашли в метод");
        User user;
        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }

        if (isEmail(identifier)) {
            user = userRepository.findUserByEmail(identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User with email " + identifier + " not found"));
            if (!user.isEmailConfirmed()) {
                throw new BadCredentialsException("Email is not confirmed to sign-in");
            }
        } else if (isPhoneNumber(identifier)) {
            return null;
        } else {
            user = userRepository.findUserByUsername(identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User with username " + identifier + " not found"));
        }
        return user;
    }


    public static boolean isEmail(String login) {
        return login != null && login.contains("@");
    }

    public static boolean isPhoneNumber(String login) {
        return login != null && login.matches("^\\+?[0-9]{10,15}$"); // Пример для проверки номера телефона
    }


}

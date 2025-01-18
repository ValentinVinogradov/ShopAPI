package com.shopapi.shop.impl;

import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.UserRepository;
import com.shopapi.shop.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User getByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email " + email));
    }

    @Override
    public void updateUsername(String currentUsername, String newUsername) throws IllegalArgumentException{
        User user = getByUsername(currentUsername);
        if (existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already exists");
        }
        userRepository.updateUsername(user.getId(), newUsername);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        userRepository.save(user);
    }

//    public void changePassword(String username, newPassword) {
//
//    }

    public boolean existsByUsername(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

//    @Override
//    public void redirectToChangePassword() {
//    }

//    @Override
//    public void updatePassword(String username, String newPassword) throws IllegalArgumentException {
//        User user = getByUsername(username);
//        if (user.getPassword().equals(newPassword)) {
//            throw new IllegalArgumentException("New password cannot be the same as the old one");
//        }
//
//        userRepository.updatePassword(user.getId(), newPassword);
//    }

    @Override
    public void updateEmail(String username, String email) {
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {
    }
}

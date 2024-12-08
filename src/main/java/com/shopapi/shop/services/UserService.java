package com.shopapi.shop.services;

import com.shopapi.shop.models.User;

import java.util.List;

public interface UserService extends GenericService<User, Long> {
    void addUser(User user);
    void updateUsername(Long userId, String username);
    void updatePassword(Long userId, String password);
    void updateEmail(Long userId, String email);
    User authenticateUser(String email, String password); // Аутентификация пользователя по email и паролю
}

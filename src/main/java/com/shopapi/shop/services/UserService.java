package com.shopapi.shop.services;

import com.shopapi.shop.models.User;

public interface UserService extends GenericService<User, Long> {
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    void changeUsername(String currentUsername, String newUsername);
    void updatePassword(User user, String newPassword);
    void changeEmail(String username, String newEmail);
}

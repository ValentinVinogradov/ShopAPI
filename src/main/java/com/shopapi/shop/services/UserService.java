package com.shopapi.shop.services;

import com.shopapi.shop.models.User;

public interface UserService extends GenericService<User, Long> {
    User getByUsername(String username);
    User getByEmail(String email);
    void updateUsername(String currentUsername, String newUsername);
    void updatePassword(User user, String newPassword);
//    void redirectToChangePassword();
//    void updatePassword(String username, String newPassword) throws BadRequestException;

//    void updateEmail(String username, String email);
}

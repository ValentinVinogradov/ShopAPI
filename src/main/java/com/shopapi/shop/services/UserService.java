package com.shopapi.shop.services;

import com.shopapi.shop.models.User;

import java.util.List;

public interface UserService extends GenericService<User, Long> {                 // Удалить всех пользователей
    User getUserByEmail(String email);        // Получить пользователя по email (если email уникален)
    void changePassword(long userId, String newPassword); // Изменить пароль пользователя
    boolean userExists(String email);         // Проверить существует ли пользователь с таким email
    User authenticateUser(String email, String password); // Аутентификация пользователя по email и паролю
}

package com.shopapi.shop.impl;

import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.models.UUIDToken;
import com.shopapi.shop.models.User;
import com.shopapi.shop.services.PasswordService;
import jakarta.persistence.EntityNotFoundException;
import org.antlr.v4.runtime.tree.pattern.TokenTagToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final UUIDTokenServiceImpl tokenService;
    private final UserServiceImpl userService;

    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(UUIDTokenServiceImpl tokenService,
                               UserServiceImpl userService,
                               PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isExistsUserEmail(String email) {
        return userService.existsByEmail(email);
    }

    @Override
    public String generateToken(String email) {
        if (isExistsUserEmail(email)) {
            return userService.generatePasswordToken(email);
        } else {
            throw new BadCredentialsException("Email doesn't exists");
        }
    }

    public String checkPasswordToken(String token) {
        return userService.checkToken(token, UUIDTokenType.PASSWORD);
    }

    //todo сделать метод удаления старых ненужных токенов


    //todo понять надо ли валидировать второй раз токен ? (наверное там как то через сессию)
    @Override
    public void saveNewPassword(String email, String newPassword) {
        User user = userService.getByEmail(email);
        userService.updatePassword(user, passwordEncoder.encode(newPassword));
        tokenService.deletePasswordTypeTokenByUserId(user.getId());
    }
}

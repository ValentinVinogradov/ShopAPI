package com.shopapi.shop.impl;

import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.models.User;
import com.shopapi.shop.services.PasswordService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final UUIDTokenServiceImpl tokenService;
    private final UserServiceImpl userService;
    private final JWTServiceImpl jwtService;

    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(UUIDTokenServiceImpl tokenService,
                               UserServiceImpl userService, JWTServiceImpl jwtService,
                               PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isExistsUserEmail(String email) {
        return userService.existsByEmail(email);
    }


    //todo подумать над тем где сделать логаут (потому что пользователь может запомнить ссылку а jwt token
    // останется
    @Override
    public String generateToken(String email) {
        if (isExistsUserEmail(email)) {
            return userService.generateToken(email, UUIDTokenType.PASSWORD);
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
        User user = userService.getUserByEmail(email);
        userService.updatePassword(user, passwordEncoder.encode(newPassword));
        tokenService.deleteAllTokensWithTypeByUserId(user.getId(), UUIDTokenType.PASSWORD);
        userService.revokeAllUserAccessTokens(user);
    }
}

package com.shopapi.shop.impl;

import com.shopapi.shop.models.UUIDToken;
import com.shopapi.shop.models.User;
import com.shopapi.shop.services.PasswordService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final UUIDTokenServiceImpl tokenService;
    private final UserServiceImpl userService;
//    private final UUIDTokenRepository tokenRepository;

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
    public String generateToken(String email) throws BadCredentialsException {
        if (isExistsUserEmail(email)) {
            User user = userService.getByEmail(email);
            revokeUserUUIDToken(user.getId());
            UUIDToken token = tokenService.generateToken(user);
            return token.getToken();
        } else {
            throw new BadCredentialsException("Email doesn't exists");
        }
    }

    private void revokeUserUUIDToken(long userId) {
        if (tokenService.existsTokenByUserId(userId)) {
            tokenService.deleteUUIDTokenByUserId(userId);
        }
    }

    //todo сделать метод удаления старых ненужных токенов

    public String checkToken(String token) {
        try {
            UUIDToken storedToken = tokenService.getToken(token);
            if (tokenService.isValidUUIDToken(storedToken)) {
                return "Success!";
            } else {
                //todo удаление по токену
                //todo надо ли удалять токен после неудачной проверки?
//                revokeUserUUIDToken();
                throw new BadCredentialsException("Token was expired");
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Token not found");
        }
    }

    //todo понять надо ли валидировать второй раз токен ?
    @Override
    public void saveNewPassword(String email, String newPassword) {
        User user = userService.getByEmail(email);
        userService.updatePassword(user, passwordEncoder.encode(newPassword));
        tokenService.deleteUUIDTokenByUserId(user.getId());
    }
}

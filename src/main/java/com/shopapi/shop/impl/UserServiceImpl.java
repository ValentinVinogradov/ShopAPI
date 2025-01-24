package com.shopapi.shop.impl;

import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.models.UUIDToken;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.UserRepository;
import com.shopapi.shop.services.UUIDTokenService;
import com.shopapi.shop.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UUIDTokenServiceImpl tokenService;


    public UserServiceImpl(UserRepository userRepository,
                           UUIDTokenServiceImpl tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
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

    //todo сделать удаление токена по определенному типу (короче дописать как то красиво я пока набросил)

    //todo переосмыслить весь код этот, нужны ли перечисления и можно ли просто разделить на несколько методов
    // гпт сказал можно разделить на методы значит разделяем на методы

    public void revokeToken(long userId, UUIDTokenType tokenType) {
        if (tokenService.existsTokenByUserId(userId)) {
            switch (tokenType) {
                case PASSWORD:
                    tokenService.deletePasswordTypeTokenByUserId(userId);
                case EMAIL:
                    tokenService.deleteEmailTypeTokenByUserId(userId);
            }
        }
    }

    public String generateToken(User user) {
        UUIDToken token = tokenService.generateToken(user);
        return token.getToken();
    }

    public String generateEmailToken(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        revokeToken(user.getId(), UUIDTokenType.EMAIL);
        return generateToken(user);
    }

    public String generatePasswordToken(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        revokeToken(user.getId(), UUIDTokenType.PASSWORD);
        return generateToken(user);
    }

    public String checkToken(String token, UUIDTokenType tokenType) {
        try {
            UUIDToken storedToken = tokenService.getToken(token);
            if (tokenService.isValidUUIDToken(storedToken)) {
                return "Success!";
            } else {
                switch (tokenType) {
                    case PASSWORD:
                        tokenService.deletePasswordTypeTokenByUserId(storedToken.getUser().getId());
                    case EMAIL:
                        tokenService.deleteEmailTypeTokenByUserId(storedToken.getUser().getId());
                }
                throw new BadCredentialsException("Token was expired");
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Token not found");
        }
    }


    public boolean existsByUsername(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }


    //todo придумать как связать смену почты и пароля используя похожие методы
//    @Override
//    public void updateEmail(String username, String email) {
//        if (existsByEmail(email)) {
//            throw new BadCredentialsException("Email already exists");
//        }
//        User user = getByEmail(email);
//        revokeUserUUIDToken(user.getId());
//        UUIDToken token = tokenService.generateToken(user);
//        return token.getToken();
//    }

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

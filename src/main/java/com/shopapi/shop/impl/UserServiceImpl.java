package com.shopapi.shop.impl;

import com.shopapi.shop.dto.JWTTokenResponseDTO;
import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.models.JWTToken;
import com.shopapi.shop.models.UUIDToken;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.JWTTokenRepository;
import com.shopapi.shop.repositories.UserRepository;
import com.shopapi.shop.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UUIDTokenServiceImpl UUIDTokenService;
    private final JWTServiceImpl jwtService;
    private final JWTTokenRepository jwtTokenRepository;



    public UserServiceImpl(UserRepository userRepository,
                           UUIDTokenServiceImpl UUIDTokenService,
                           JWTServiceImpl jwtService,
                           JWTTokenRepository jwtTokenRepository) {
        this.userRepository = userRepository;
        this.UUIDTokenService = UUIDTokenService;
        this.jwtService = jwtService;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    //todo потом везде транз. методы пометить аннотацией Transactional

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email " + email));
    }

    @Override
    @Transactional
    public void changeUsername(String currentUsername, String newUsername) throws IllegalArgumentException{
        User user = getUserByUsername(currentUsername);
        if (existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setUsername(newUsername);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void revokeAllTokens(UUID userId, UUIDTokenType tokenType) {
        if (UUIDTokenService.existsTokenByUserId(userId)) {
            UUIDTokenService.deleteAllTokensWithTypeByUserId(userId, tokenType);
        }
    }


    public void revokeAllUserAccessTokens(User user) {
        List<JWTToken> validUserAccessTokens = jwtTokenRepository
                .findAllByUser_Id(user.getId());
        if (!validUserAccessTokens.isEmpty()) {
//            validUserAccessTokens.forEach(t -> {t.setLoggedOut(true);});
            jwtService.deleteAllUserTokens(user);
        }

        jwtTokenRepository.saveAll(validUserAccessTokens);
    }

    public String generateToken(String email, UUIDTokenType tokenType) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        revokeAllTokens(user.getId(), tokenType); //todo это под вопросом надо ли (вроде надо при повторной генерации)
        UUIDToken token = UUIDTokenService.generateToken(user, tokenType);
        return token.getToken();
    }

//    public String checkToken(String token, UUIDTokenType tokenType) {
//        try {
//            UUIDToken storedToken = UUIDTokenService.getToken(token);
//            User user = storedToken.getUser();
//            if (UUIDTokenService.isValidUUIDToken(storedToken)) {
//                if (tokenType == UUIDTokenType.EMAIL) {
//                    user.setEmailConfirmed(true);
//                    userRepository.save(user);
//                    UUIDTokenService.deleteAllTokensWithTypeByUserId(user.getId(), tokenType);
//                }
//                return "Success!";
//            } else {
//                UUIDTokenService.deleteAllTokensWithTypeByUserId(
//                        storedToken.getUser().getId(),
//                        tokenType);
//                throw new BadCredentialsException("Token was expired");
//            }
//        } catch (EntityNotFoundException e) {
//            throw new EntityNotFoundException("Token not found");
//        }
//    }
//
//    public JWTTokenReponseDTO checkSignUpToke(String token) {
//        try {
//            UUIDToken storedToken = UUIDTokenService.getToken(token);
//            User user = storedToken.getUser();
//            if (UUIDTokenService.isValidUUIDToken(storedToken)) {
//                user.setEmailConfirmed(true);
//                user.setActive(true);
//                userRepository.save(user);
//                UUIDTokenService.deleteAllTokensWithTypeByUserId(user.getId(), UUIDTokenType.EMAIL);
//                String accessToken = jwtService.generateAccessToken(user);
//                String refreshToken = jwtService.generateRefreshToken(user);
//                return new JWTTokenReponseDTO(accessToken, refreshToken);
//            } else {
//                UUIDTokenService.deleteAllTokensWithTypeByUserId(
//                        storedToken.getUser().getId(),
//                        UUIDTokenType.EMAIL);
//                throw new BadCredentialsException("Token was expired");
//            }
//        } catch (EntityNotFoundException e) {
//            throw new EntityNotFoundException("Token not found");
//        }
//    }

    public String checkToken(String token, UUIDTokenType tokenType) {
        validateAndActivateUser(token, tokenType);
        return "Success!";
    }

    public JWTTokenResponseDTO checkSignUpToken(String token) {
        User user = validateAndActivateUser(token, UUIDTokenType.EMAIL);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        jwtService.saveUserTokens(user, accessToken, refreshToken);
        return new JWTTokenResponseDTO(accessToken, refreshToken);
    }

    private User validateAndActivateUser(String token, UUIDTokenType tokenType) {
        UUIDToken storedToken = UUIDTokenService.getToken(token);
        User user = storedToken.getUser();

        if (!UUIDTokenService.isValidUUIDToken(storedToken)) {
            UUIDTokenService.deleteAllTokensWithTypeByUserId(user.getId(), tokenType);
            throw new BadCredentialsException("Token was expired");
        }

        if (tokenType == UUIDTokenType.EMAIL) {
            user.setActive(true);
            user.setEmailConfirmed(true);
            userRepository.save(user);
        }

        UUIDTokenService.deleteAllTokensWithTypeByUserId(user.getId(), tokenType);

        return user;
    }

    public boolean existsByUsername(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    //todo подумать над этим
    @Override
    public void changeEmail(String username, String newEmail) {
        if (existsByEmail(newEmail)) {
            throw new BadCredentialsException("Email already exists");
        }
        User user = getUserByUsername(username);
        user.setEmail(newEmail);
        user.setEmailConfirmed(false);
        userRepository.save(user);
    }


    //todo подумать какие данные возвращать

    public List<User> getAll() {
        return List.of();
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//        return new UserResponseDTO(userId, user.getUsername());
    }

    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
    }
}

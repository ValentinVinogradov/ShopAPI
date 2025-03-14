package com.shopapi.shop.impl;

import com.shopapi.shop.dto.JWTTokenResponseDTO;
import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.models.Role;
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
    private final RoleServiceImpl roleService;



    public UserServiceImpl(UserRepository userRepository,
                           UUIDTokenServiceImpl UUIDTokenService,
                           JWTServiceImpl jwtService,
                           JWTTokenRepository jwtTokenRepository,
                           RoleServiceImpl roleService) {
        this.userRepository = userRepository;
        this.UUIDTokenService = UUIDTokenService;
        this.jwtService = jwtService;
        this.jwtTokenRepository = jwtTokenRepository;
        this.roleService = roleService;
    }

    public User createUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }

    //todo потом везде транз. методы пометить аннотацией Transactional

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public User getUserByOauth2Id(String oauth2Id) {
        return userRepository.findUserByOauth2Id(oauth2Id)
                .orElse(null);
    }

    public User getUserByIdentifier(String identifier) {
        System.out.println("Зашли в метод load");
        User user;
        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }

        if (isEmail(identifier)) {
            user = getUserByEmail(identifier);
            if (!user.isEmailConfirmed()) {
                throw new BadCredentialsException("Email is not confirmed to sign-in");
            }
        } else if (isPhoneNumber(identifier)) {
            return null;
        } else {
            user = getUserByUsername(identifier);
        }
        return user;
    }


    public static boolean isEmail(String login) {
        return login != null && login.contains("@");
    }

    public static boolean isPhoneNumber(String login) {
        return login != null && login.matches("^\\+?[0-9]{10,15}$"); // Пример для проверки номера телефона
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

    public void saveUser(User user) {
        userRepository.save(user);
    }


    public String generateToken(String email, UUIDTokenType tokenType) {
        User user = getUserByEmail(email);
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

    @Override
    public void createNewOAuthUser(String email, String username, String avatarUrl) {
        User user = new User();
        Role userRole = roleService.getRoleByName("USER");
        // Установить роль по умолчанию
        user.getRoles().add(userRole);
        user.setUsername(username);
        user.setEmail(email);
        System.out.println(avatarUrl);
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

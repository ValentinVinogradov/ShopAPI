package com.shopapi.shop.impl;

import com.shopapi.shop.dto.JWTResponseDTO;
import com.shopapi.shop.dto.UserSignInRequestDTO;
import com.shopapi.shop.dto.UserSignUpRequestDTO;
import com.shopapi.shop.models.JWTToken;
import com.shopapi.shop.models.Role;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.JWTTokenRepository;
import com.shopapi.shop.repositories.RoleRepository;
import com.shopapi.shop.repositories.UserRepository;
import com.shopapi.shop.services.UserAuthService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTTokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JWTServiceImpl jwtService;

    public UserAuthServiceImpl(UserRepository userRepository,
                               RoleRepository roleRepository,
                               JWTTokenRepository tokenRepository,
                               PasswordEncoder passwordEncoder,
                               AuthenticationManager authenticationManager,
                               JWTServiceImpl jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public JWTResponseDTO signUp(UserSignUpRequestDTO userSignUpRequestDTO) {
        User user = new User();
        Role userRole = roleRepository.findRoleByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Роль 'ROLE_USER' не найдена"));

        // Установить роль по умолчанию
        user.getRoles().add(userRole);
        user.setUsername(userSignUpRequestDTO.getUsername());
        user.setEmail(userSignUpRequestDTO.getEmail());
        String hashedPassword = passwordEncoder.encode(userSignUpRequestDTO.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        //todo подумать над разделением сеансов либо удалять вообще токены

        revokeAllUserAccessTokens(user);

        saveUserTokens(user, accessToken, refreshToken);

        return new JWTResponseDTO(accessToken, refreshToken);
    }

    @Override
    public JWTResponseDTO signIn(UserSignInRequestDTO userSignInRequestDTO) {
        String login = userSignInRequestDTO.getLogin();
        String password = userSignInRequestDTO.getPassword();
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(login, password));
        if (authentication.isAuthenticated()) {
            User user = getUserByLogin(login);
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);
                revokeAllUserAccessTokens(user);
                saveUserTokens(user, accessToken, refreshToken);
                return new JWTResponseDTO(accessToken, refreshToken); //username or else???
            }
        } else {
            System.out.println("не вошли");
            return null;
        }
    }

    private void revokeAllUserAccessTokens(User user) {
        List<JWTToken> validUserAccessTokens = tokenRepository
                .findAllByUser_Id(user.getId());
        if (!validUserAccessTokens.isEmpty()) {
//            validUserAccessTokens.forEach(t -> {t.setLoggedOut(true);});
            jwtService.deleteAllUserTokens(user);
        }

        tokenRepository.saveAll(validUserAccessTokens);
    }

    private void saveUserTokens(User user, String accessToken, String refreshToken) {
        JWTToken jwtToken = new JWTToken();
        jwtToken.setUser(user);
        jwtToken.setAccessToken(accessToken);
        jwtToken.setRefreshToken(refreshToken);
//        jwtToken.setLoggedOut(false);

        tokenRepository.save(jwtToken);
    }

    //todo подумать над тем получится ли вынести код внутри этого всего про авторизацию
    @Override
    public JWTResponseDTO refreshToken(HttpServletRequest request) throws ExpiredJwtException, IllegalArgumentException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        String username;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return null;
        }
        try {
            refreshToken = authHeader.substring(7);
            username = jwtService.getUsernameFromToken(refreshToken);
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

            if (jwtService.isValidRefreshToken(refreshToken, user)) {
                String newAccessToken = jwtService.generateAccessToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);
                revokeAllUserAccessTokens(user);
                saveUserTokens(user, newAccessToken, newRefreshToken);
                return new JWTResponseDTO(newAccessToken, newRefreshToken);
            } else {
                throw new IllegalArgumentException("Invalid refresh token");
            }
        } catch (ExpiredJwtException e) {
            throw e; // Прокидываем исключение дальше
        } catch (Exception e) {
            throw new IllegalArgumentException("Token processing failed: " + e.getMessage(), e);
        }
    }



    public boolean existsByUsername(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    private User getUserByLogin(String login) {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }

        if (isEmail(login)) {
            return userRepository.findUserByEmail(login)
                    .orElseThrow(() -> new EntityNotFoundException("User with email " + login + " not found"));
        } else if (isPhoneNumber(login)) {
            // Допиши обработку поиска по номеру телефона
            return null;
        } else {
            return userRepository.findUserByUsername(login)
                    .orElseThrow(() -> new EntityNotFoundException("User with username " + login + " not found"));
        }
    }

    public static boolean isEmail(String login) {
        return login != null && login.contains("@");
    }

    public static boolean isPhoneNumber(String login) {
        return login != null && login.matches("^\\+?[0-9]{10,15}$"); // Пример для проверки номера телефона
    }

//    public static boolean isUsername(String login) {
//        return login != null && !isEmail(login) && !isPhoneNumber(login);
//    }
}

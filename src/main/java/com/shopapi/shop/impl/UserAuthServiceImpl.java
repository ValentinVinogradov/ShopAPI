package com.shopapi.shop.impl;

import com.shopapi.shop.dto.TokenResponseDTO;
import com.shopapi.shop.dto.UserSignInRequestDTO;
import com.shopapi.shop.dto.UserSignUpRequestDTO;
import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.models.Role;
import com.shopapi.shop.models.User;
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

@Service
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTServiceImpl jwtService;
    private final UUIDTokenServiceImpl UUIDTokenService;

    public UserAuthServiceImpl(UserRepository userRepository,
                               RoleRepository roleRepository,
                               UserServiceImpl userService,
                               PasswordEncoder passwordEncoder,
                               AuthenticationManager authenticationManager,
                               JWTServiceImpl jwtService,
                               UUIDTokenServiceImpl UUIDTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.UUIDTokenService = UUIDTokenService;
    }

    public TokenResponseDTO signUp(UserSignUpRequestDTO userSignUpRequestDTO) {
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
        String UUIDToken = UUIDTokenService.generateToken(user, UUIDTokenType.EMAIL).getToken();

        //todo надо ли при регистрации что то удалять?
//        userService.revokeAllUserAccessTokens(user);

        jwtService.saveUserTokens(user, accessToken, refreshToken);

        return new TokenResponseDTO(accessToken, refreshToken, UUIDToken);
    }

    @Override
    public TokenResponseDTO signIn(UserSignInRequestDTO userSignInRequestDTO) {
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
                userService.revokeAllUserAccessTokens(user);
                jwtService.saveUserTokens(user, accessToken, refreshToken);

                //todo подумать как убрать этот null наверное
                return new TokenResponseDTO(accessToken, refreshToken, null);
            }
        } else {
            System.out.println("не вошли");
            return null;
        }
    }

//    private void revokeAllUserAccessTokens(User user) {
//        List<JWTToken> validUserAccessTokens = jwtTokenRepository
//                .findAllByUser_Id(user.getId());
//        if (!validUserAccessTokens.isEmpty()) {
////            validUserAccessTokens.forEach(t -> {t.setLoggedOut(true);});
//            jwtService.deleteAllUserTokens(user);
//        }
//
//        jwtTokenRepository.saveAll(validUserAccessTokens);
//    }



    //todo подумать над тем получится ли вынести код внутри этого всего про авторизацию
    @Override
    public TokenResponseDTO refreshToken(HttpServletRequest request) throws ExpiredJwtException, IllegalArgumentException {
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
                userService.revokeAllUserAccessTokens(user);
                jwtService.saveUserTokens(user, newAccessToken, newRefreshToken);
                return new TokenResponseDTO(newAccessToken, newRefreshToken, null);
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

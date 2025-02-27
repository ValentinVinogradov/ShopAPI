package com.shopapi.shop.impl;

import com.shopapi.shop.dto.JWTTokenResponseDTO;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTServiceImpl jwtService;
    private final UUIDTokenServiceImpl UUIDTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    public UserAuthServiceImpl(UserRepository userRepository,
                               RoleRepository roleRepository,
                               UserServiceImpl userService,
                               PasswordEncoder passwordEncoder,
                               AuthenticationManager authenticationManager,
                               JWTServiceImpl jwtService,
                               UUIDTokenServiceImpl UUIDTokenService,
                               UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.UUIDTokenService = UUIDTokenService;
        this.userDetailsService = userDetailsService;
    }

    //todo сделать принудительное подтверждение почты
//    public FullTokenResponseDTO signUp(UserSignUpRequestDTO userSignUpRequestDTO) {
//        User user = new User();
//        Role userRole = roleRepository.findRoleByName("ROLE_USER")
//                .orElseThrow(() -> new EntityNotFoundException("Роль 'ROLE_USER' не найдена"));
//
//        // Установить роль по умолчанию
//        user.getRoles().add(userRole);
//        user.setUsername(userSignUpRequestDTO.getUsername());
//        user.setEmail(userSignUpRequestDTO.getEmail());
//        String hashedPassword = passwordEncoder.encode(userSignUpRequestDTO.getPassword());
//        user.setPassword(hashedPassword);
//        userRepository.save(user);
//
//        String accessToken = jwtService.generateAccessToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//        String UUIDToken = UUIDTokenService.generateToken(user, UUIDTokenType.EMAIL).getToken();
//
//        //todo надо ли при регистрации что то удалять? (наверное нет)
////        userService.revokeAllUserAccessTokens(user);
//
//        jwtService.saveUserTokens(user, accessToken, refreshToken);
//
//        return new FullTokenResponseDTO(accessToken, refreshToken, UUIDToken);
//    }
    //todo подумать над тем чтобы выдавать здесь jwt токены
    public String signUp(UserSignUpRequestDTO userSignUpRequestDTO) {
        User user = new User();
        Role userRole = roleRepository.findRoleByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Роль 'ROLE_USER' не найдена"));

        // Установить роль по умолчанию
        user.getRoles().add(userRole);
        user.setUsername(userSignUpRequestDTO.username());
        user.setEmail(userSignUpRequestDTO.email());
        String hashedPassword = passwordEncoder.encode(userSignUpRequestDTO.password());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return UUIDTokenService.generateToken(user, UUIDTokenType.EMAIL).getToken();
    }

    //todo придумать как входить по почте и др способом
    //todo придумать как дореализовать получение пользователя
    //todo все это выполнил осталось проверить и поломать все это
    @Override
    public JWTTokenResponseDTO signIn(UserSignInRequestDTO userSignInRequestDTO) {
        try {
            String login = userSignInRequestDTO.login();
            System.out.println("Login: " + login);
            String password = userSignInRequestDTO.password();
            System.out.println("Password: " + password);
            User user = userDetailsService.loadUserByIdentifier(login);
            System.out.println("User: " + user.toString());
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login, password));
            System.out.println("authentication.isAuthenticated(): " + authentication.isAuthenticated());
            if (authentication.isAuthenticated()) {
                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                userService.revokeAllUserAccessTokens(user);
                jwtService.saveUserTokens(user, accessToken, refreshToken);

                return new JWTTokenResponseDTO(accessToken, refreshToken);
            } else {
                System.out.println("не вошли");
                return null;
            }
//        String login = userSignInRequestDTO.getLogin();
//        System.out.println("Login: " + login);
//        String password = userSignInRequestDTO.getPassword();
//        System.out.println("Password: " + password);
//        try {
//            User user = loadUserByIdentifier(login);
//            System.out.println("User: " + user.toString());
//            UserPrincipal userPrincipal = new UserPrincipal(user);
//            System.out.println("UserPrincipal: " + userPrincipal);
//            if (login == null || login.isEmpty()) {
//                throw new IllegalArgumentException("Login cannot be null or empty");
//            }
//
//            Authentication authentication = authenticationManager.
//                    authenticate(new UsernamePasswordAuthenticationToken(
//                            userPrincipal,
//                            password,
//                            userPrincipal.getAuthorities()));
//            System.out.println("Authentication: " + authentication);
//            System.out.println("authentication.isAuthenticated(): " + authentication.isAuthenticated());

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User not found");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public User loadUserByIdentifier(String identifier) {
        System.out.println("Зашли в метод");
        User user;
        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }

        if (isEmail(identifier)) {
            user = userRepository.findUserByEmail(identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User with email " + identifier + " not found"));
        } else if (isPhoneNumber(identifier)) {
            // Допиши обработку поиска по номеру телефона
            return null;
        } else {
            user = userRepository.findUserByUsername(identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User with username " + identifier + " not found"));
        }
        return user;
    }

    public static boolean isEmail(String login) {
        return login != null && login.contains("@");
    }

    public static boolean isPhoneNumber(String login) {
        return login != null && login.matches("^\\+?[0-9]{10,15}$"); // Пример для проверки номера телефона
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
    public JWTTokenResponseDTO refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        UUID userId;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return null;
        }
        try {
            refreshToken = authHeader.substring(7);
            userId = jwtService.getUserIdFromToken(refreshToken);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

            if (jwtService.isValidRefreshToken(refreshToken, user)) {
                String newAccessToken = jwtService.generateAccessToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);
                userService.revokeAllUserAccessTokens(user);
                jwtService.saveUserTokens(user, newAccessToken, newRefreshToken);
                return new JWTTokenResponseDTO(newAccessToken, newRefreshToken);
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

//    private User getUserByLogin(String login) {
//        if (login == null || login.isEmpty()) {
//            throw new IllegalArgumentException("Login cannot be null or empty");
//        }
//
//        if (isEmail(login)) {
//            return userRepository.findUserByEmail(login)
//                    .orElseThrow(() -> new EntityNotFoundException("User with email " + login + " not found"));
//        } else if (isPhoneNumber(login)) {
//            // Допиши обработку поиска по номеру телефона
//            return null;
//        } else {
//            return userRepository.findUserByUsername(login)
//                    .orElseThrow(() -> new EntityNotFoundException("User with username " + login + " not found"));
//        }
//    }

//    public static boolean isUsername(String login) {
//        return login != null && !isEmail(login) && !isPhoneNumber(login);
//    }
}

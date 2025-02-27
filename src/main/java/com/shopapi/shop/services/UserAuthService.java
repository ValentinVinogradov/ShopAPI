package com.shopapi.shop.services;

import com.shopapi.shop.dto.JWTTokenResponseDTO;
import com.shopapi.shop.dto.UserSignInRequestDTO;
import com.shopapi.shop.dto.UserSignUpRequestDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;

public interface UserAuthService {
//    FullTokenResponseDTO signUp(UserSignUpRequestDTO userSignUpRequestDTO);
    String signUp(UserSignUpRequestDTO userSignUpRequestDTO);
    JWTTokenResponseDTO signIn(UserSignInRequestDTO userSignInRequestDTO) throws BadCredentialsException;
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    JWTTokenResponseDTO refreshToken(HttpServletRequest request) throws ExpiredJwtException, IllegalArgumentException;
}

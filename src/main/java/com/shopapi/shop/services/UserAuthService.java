package com.shopapi.shop.services;

import com.shopapi.shop.dto.TokenResponseDTO;
import com.shopapi.shop.dto.UserSignInRequestDTO;
import com.shopapi.shop.dto.UserSignUpRequestDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;

public interface UserAuthService {
    TokenResponseDTO signUp(UserSignUpRequestDTO userSignUpRequestDTO);
    TokenResponseDTO signIn(UserSignInRequestDTO userSignInRequestDTO) throws BadCredentialsException;
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    TokenResponseDTO refreshToken(HttpServletRequest request) throws ExpiredJwtException, IllegalArgumentException;
}

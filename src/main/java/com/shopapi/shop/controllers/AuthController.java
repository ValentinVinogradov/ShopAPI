package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.UserSignInRequestDTO;
import com.shopapi.shop.dto.UserSignUpRequestDTO;
import com.shopapi.shop.impl.UserAuthServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private final UserAuthServiceImpl authService;

    public AuthController(UserAuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpRequestDTO userSignUpRequestDTO) {
        if (authService.existsByEmail(userSignUpRequestDTO.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists with email: " + userSignUpRequestDTO.email());
        }
        if (authService.existsByUsername(userSignUpRequestDTO.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists with username: " + userSignUpRequestDTO.username());
        }
        try {
            URI uri = new URI("/auth/v1");
            return ResponseEntity.created(uri).body(authService.signUp(userSignUpRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserSignInRequestDTO userSignInRequestDTO) {
        try {
            return ResponseEntity.ok(authService.signIn(userSignInRequestDTO));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to sign-in: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(authService.refreshToken(request));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Failed to refresh access token: " + e.getMessage());
        }
    }
}

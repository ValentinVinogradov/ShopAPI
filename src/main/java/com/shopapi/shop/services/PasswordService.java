package com.shopapi.shop.services;

import org.springframework.security.authentication.BadCredentialsException;

public interface PasswordService {
    boolean isExistsUserEmail(String email);

    String generateToken(String email) throws BadCredentialsException;

    String checkToken(String token) throws BadCredentialsException;

    void saveNewPassword(String email, String newPassword);
}

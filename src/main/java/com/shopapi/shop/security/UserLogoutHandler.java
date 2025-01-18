package com.shopapi.shop.security;

import com.shopapi.shop.repositories.JWTTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class UserLogoutHandler implements LogoutHandler {

    private final JWTTokenRepository tokenRepository;

    public UserLogoutHandler(JWTTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return;
        }
        String token = header.substring(7);
        if (tokenRepository.findJWTTokenByAccessToken(token).isPresent()) {
            tokenRepository.deleteByToken(token);
//            storedToken.setLoggedOut(true);
//            tokenRepository.save(storedToken);
        }
    }
}

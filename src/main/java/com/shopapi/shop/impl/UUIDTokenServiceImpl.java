package com.shopapi.shop.impl;

import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.models.UUIDToken;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.UUIDTokenRepository;
import com.shopapi.shop.services.UUIDTokenService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UUIDTokenServiceImpl implements UUIDTokenService {

    @Value("${app.security.uuid.token-expiration}")
    private long UUIDTokenExpiration;

    private final UUIDTokenRepository tokenRepository;

    public UUIDTokenServiceImpl(UUIDTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public UUIDToken generateToken(User user, UUIDTokenType tokenType) {
        UUIDToken token = new UUIDToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setTokenType(tokenType);
        token.setExpiresAt(new Date(System.currentTimeMillis() + UUIDTokenExpiration));
        tokenRepository.save(token);
        return token;
    }

    public boolean isValidUUIDToken(UUIDToken token) {
        return token.getExpiresAt().after(new Date());
    }

    public UUIDToken getToken(String token) {
        return tokenRepository.findUUIDTokenByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("UUID token not found"));
    }

    public void deleteAllTokensWithTypeByUserId(long userId, UUIDTokenType tokenType) {
        tokenRepository.deleteAllTokensWithTypeByUserId(userId, tokenType);
    }

    public boolean existsTokenByUserId(long userId) {
        return tokenRepository.findUUIDTokenByUser_Id(userId).isPresent();
    }
}
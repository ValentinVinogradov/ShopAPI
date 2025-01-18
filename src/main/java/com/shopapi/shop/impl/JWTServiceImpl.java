package com.shopapi.shop.impl;

import com.shopapi.shop.models.Role;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.JWTTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTServiceImpl {

    @Value("${app.security.jwt.secret}")
    private String secretKey;

    @Value("${app.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final JWTTokenRepository tokenRepository;

    public JWTServiceImpl(JWTTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration, "access");
    }


    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, "refresh");
    }

    private String generateToken(User user, long expirationTime, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", getUserRoles(user));
        claims.put("type", tokenType);
        return Jwts.builder()
                .subject(user.getUsername())
                .claims().add(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Set<String> getUserRoles(User user) {
        Set<Role> roles = user.getRoles();
        return roles
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValidAccessToken(String accessToken, UserDetails userDetails) {
        System.out.println("зашли в проверку");
        final String username = getUsernameFromToken(accessToken);
        System.out.println(username);
        boolean isValidToken = tokenRepository.findJWTTokenByAccessToken(accessToken).isPresent();
        System.out.println(isValidToken);
        //        boolean isValidTokenByLogOut = tokenRepository.findByAccessToken(accessToken).map(t -> !t.isLoggedOut()).orElse(false);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(accessToken) && isValidToken;
    }

    public boolean isValidRefreshToken(String refreshToken, User user) {
        final String username = getUsernameFromToken(refreshToken);
        boolean isValidToken = tokenRepository.findJWTTokenByRefreshToken(refreshToken).isPresent();
        //        boolean isValidTokenByLogOut = tokenRepository.findByRefreshToken(refreshToken).map(t -> !t.isLoggedOut()).orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(refreshToken) && isValidToken;
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    //todo сделать удаление по типу устройства или че то типо такого потом
    public void deleteAllUserTokens(User user) {
        tokenRepository.deleteByUserId(user.getId());
    }
}

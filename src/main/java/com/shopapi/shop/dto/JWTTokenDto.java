package com.shopapi.shop.dto;


/**
 * DTO for {@link com.shopapi.shop.models.JWTToken}
 */
public record JWTTokenDto(
        Long id,
        String accessToken,
        String refreshToken
) {}
package com.shopapi.shop.dto;


public record FullTokenResponseDTO (
    String accessToken,

    String refreshToken,

    String uuidToken
) {}

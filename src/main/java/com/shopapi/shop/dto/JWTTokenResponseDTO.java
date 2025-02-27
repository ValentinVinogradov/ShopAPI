package com.shopapi.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//todo разбить наверное на две дто для jwt и для всех троих
public record JWTTokenResponseDTO(
        @JsonProperty("access-token") String accessToken,
        @JsonProperty("refresh-token") String refreshToken) {
}

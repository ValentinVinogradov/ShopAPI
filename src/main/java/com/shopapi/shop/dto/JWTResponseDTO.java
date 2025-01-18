package com.shopapi.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class JWTResponseDTO {
    @JsonProperty("access-token")
    String accessToken;

    @JsonProperty("refresh-token")
    String refreshToken;
}

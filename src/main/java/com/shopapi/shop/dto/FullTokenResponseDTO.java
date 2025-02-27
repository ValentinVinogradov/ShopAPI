package com.shopapi.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FullTokenResponseDTO (
    @JsonProperty("access-token")
    String accessToken,

    @JsonProperty("refresh-token")
    String refreshToken,

    @JsonProperty("uuid-token")
    String uuidToken
) {}

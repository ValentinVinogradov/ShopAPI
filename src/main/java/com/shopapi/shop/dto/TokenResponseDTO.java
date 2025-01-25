package com.shopapi.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class TokenResponseDTO {
    @JsonProperty("access-token")
    String accessToken;

    @JsonProperty("refresh-token")
    String refreshToken;

    @JsonProperty("uuid-token")
    String uuidToken;
}

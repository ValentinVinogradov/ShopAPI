package com.shopapi.shop.dto;

import lombok.Value;

/**
 * DTO for {@link com.shopapi.shop.models.User}
 */
@Value
public class UserSignUpRequestDTO {
    String username;
    String email;
    String password;
}
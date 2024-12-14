package com.shopapi.shop.dto;

import lombok.Value;

/**
 * DTO for {@link com.shopapi.shop.models.User}
 */
@Value
public class UserResponseDTO {
    Long id;
    String username;
}
package com.shopapi.shop.dto;


/**
 * DTO for {@link com.shopapi.shop.models.User}
 */
public record UserSignUpRequestDTO(
        String username,
        String email,
        String password
) {}
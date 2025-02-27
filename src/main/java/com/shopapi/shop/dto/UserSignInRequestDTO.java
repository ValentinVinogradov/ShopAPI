package com.shopapi.shop.dto;


/**
 * DTO for {@link com.shopapi.shop.models.User}
 */
public record UserSignInRequestDTO(
        String login,
        String password
) {}
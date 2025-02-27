package com.shopapi.shop.dto;


import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.User}
 */
public record UserResponseDTO(
        UUID id,
        String username
) {}
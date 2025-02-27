package com.shopapi.shop.dto;


import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.shopapi.shop.models.User}
 */

//todo переделать
public record UserFullResponseDTO (
    String username,
    String email,
    boolean isEmailConfirmed,
    Set<RoleResponseDTO> roles,
    List<JWTTokenDto> tokens
) {}
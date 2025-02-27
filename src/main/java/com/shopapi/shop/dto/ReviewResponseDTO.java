package com.shopapi.shop.dto;


import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.Review}
 */
public record ReviewResponseDTO(
        Long id,
        UUID productId,
        UserResponseDTO user,
        String username,
        Integer rating,
        String dignities,
        String flaws,
        String content,
        LocalDate date
) {}
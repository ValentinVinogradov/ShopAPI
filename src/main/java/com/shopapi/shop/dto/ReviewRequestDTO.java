package com.shopapi.shop.dto;


import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.Review}
 */
public record ReviewRequestDTO(
        UUID productId,
        Integer rating,
        String content,
        String dignities,
        String flaws
) {}
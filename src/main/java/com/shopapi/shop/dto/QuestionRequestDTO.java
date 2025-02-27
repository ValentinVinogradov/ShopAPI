package com.shopapi.shop.dto;


import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.Question}
 */
public record QuestionRequestDTO(
        UUID productId,
        String content
) {}
package com.shopapi.shop.dto;

import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.Answer}
 */
public record AnswerRequestDTO(
        UUID userId,
        Long questionId,
        String content
) {}
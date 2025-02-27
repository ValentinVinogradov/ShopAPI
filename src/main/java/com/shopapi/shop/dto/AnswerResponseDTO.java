package com.shopapi.shop.dto;

import java.time.LocalDate;

/**
 * DTO for {@link com.shopapi.shop.models.Answer}
 */

public record AnswerResponseDTO(
        Long id,
        UserResponseDTO user,
        String username,
        String content,
        LocalDate createdAt
) {}
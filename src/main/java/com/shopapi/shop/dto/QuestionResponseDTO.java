package com.shopapi.shop.dto;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.Question}
 */
public record QuestionResponseDTO(
        Long id,
        UUID productId,
        UserResponseDTO user,
        String username,
        String content,
        LocalDate createdAt,
        List<AnswerResponseDTO> answers
) {}
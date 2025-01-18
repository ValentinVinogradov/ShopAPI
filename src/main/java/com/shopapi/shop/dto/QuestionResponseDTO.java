package com.shopapi.shop.dto;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.shopapi.shop.models.Question}
 */
@Value
public class QuestionResponseDTO {

    Long id;
    Long productId;
    UserResponseDTO user;
    String username;
    String content;
    LocalDate createdAt;
    List<AnswerResponseDTO> answers;
}
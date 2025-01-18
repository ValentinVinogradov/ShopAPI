package com.shopapi.shop.dto;

import lombok.Value;

import java.time.LocalDate;

/**
 * DTO for {@link com.shopapi.shop.models.Answer}
 */
@Value
public class AnswerResponseDTO {
    Long id;
    UserResponseDTO user;
    String username;
    String content;
    LocalDate createdAt;
}
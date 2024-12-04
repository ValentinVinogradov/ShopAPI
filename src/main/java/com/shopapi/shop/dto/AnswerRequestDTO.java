package com.shopapi.shop.dto;

import lombok.Value;

/**
 * DTO for {@link com.shopapi.shop.models.Answer}
 */
@Value
public class AnswerRequestDTO {
    Long questionId;
    Long userId;
    String content;
    String username;
}
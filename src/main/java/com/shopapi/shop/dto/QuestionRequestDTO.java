package com.shopapi.shop.dto;

import lombok.Value;

/**
 * DTO for {@link com.shopapi.shop.models.Question}
 */
@Value
public class QuestionRequestDTO {
    Long productId;
    Long userId;
    String content;
    String username;
}
package com.shopapi.shop.dto;

import lombok.Value;

/**
 * DTO for {@link com.shopapi.shop.models.Review}
 */
@Value
public class ReviewRequestDTO {
    Long productId;
    Long userId;
    Integer rating;
    String comment;
}
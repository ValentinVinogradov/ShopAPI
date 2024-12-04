package com.shopapi.shop.dto;

import com.shopapi.shop.models.Review;
import lombok.Value;

/**
 * DTO for {@link Review}
 */
@Value
public class ReviewResponseDTO {
    Review review;
    String username;
}
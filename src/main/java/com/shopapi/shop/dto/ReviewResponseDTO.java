package com.shopapi.shop.dto;

import lombok.Value;

import java.time.LocalDate;

/**
 * DTO for {@link com.shopapi.shop.models.Review}
 */
@Value
public class ReviewResponseDTO {
    Long id;
    Long productId;
    UserResponseDTO user;
    String username;
    Integer rating;
    String dignities;
    String flaws;
    String content;
    LocalDate date;
}
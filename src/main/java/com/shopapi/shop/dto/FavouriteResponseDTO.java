package com.shopapi.shop.dto;

import lombok.Value;

/**
 * DTO for {@link com.shopapi.shop.models.Favourite}
 */
@Value
public class FavouriteResponseDTO {
    Long id;
    Long userId;
    Long productId;
}
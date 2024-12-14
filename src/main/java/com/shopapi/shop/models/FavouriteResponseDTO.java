package com.shopapi.shop.models;

import lombok.Value;

/**
 * DTO for {@link Favourite}
 */
@Value
public class FavouriteResponseDTO {
    Long id;
    Long userId;
    Long productId;
}
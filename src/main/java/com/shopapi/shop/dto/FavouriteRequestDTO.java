package com.shopapi.shop.dto;

import lombok.Value;

/**
 * DTO for {@link com.shopapi.shop.models.Favourite}
 */
@Value
public class FavouriteRequestDTO {
    Long userId;
    Long productId;
}
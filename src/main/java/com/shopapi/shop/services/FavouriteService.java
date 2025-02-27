package com.shopapi.shop.services;

import com.shopapi.shop.dto.FavouriteRequestDTO;
import com.shopapi.shop.dto.FavouriteResponseDTO;

import java.util.List;
import java.util.UUID;

public interface FavouriteService {
    List<FavouriteResponseDTO> getFavouritesByUserId(UUID userId);
    void addFavourite(FavouriteRequestDTO favouriteRequestDTO);
    void deleteFavouriteById(UUID userId, UUID productId);
}

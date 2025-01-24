package com.shopapi.shop.services;

import com.shopapi.shop.dto.FavouriteRequestDTO;
import com.shopapi.shop.dto.FavouriteResponseDTO;

import java.util.List;

public interface FavouriteService {
    List<FavouriteResponseDTO> getFavouritesByUserId(long userId);
    FavouriteResponseDTO getFavouriteById(long id);
    void addFavourite(FavouriteRequestDTO favouriteRequestDTO);
    void deleteFavouriteById(long favouriteId);
}

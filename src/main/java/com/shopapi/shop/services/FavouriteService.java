package com.shopapi.shop.services;

import com.shopapi.shop.dto.FavouriteRequestDTO;
import com.shopapi.shop.models.FavouriteResponseDTO;

import java.util.List;

public interface FavouriteService {
    List<FavouriteResponseDTO> getFavouritesByUser(long userId);
    FavouriteResponseDTO getFavouriteById(long id);
    void addFavourite(FavouriteRequestDTO favouriteRequestDTO);
    void deleteFavourite(FavouriteRequestDTO favouriteRequestDTO);
}

package com.shopapi.shop.impl;

import com.shopapi.shop.dto.CartItemResponseDTO;
import com.shopapi.shop.dto.FavouriteRequestDTO;
import com.shopapi.shop.models.Favourite;
import com.shopapi.shop.models.FavouriteResponseDTO;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repository.FavouriteRepository;
import com.shopapi.shop.services.FavouriteService;
import com.shopapi.shop.services.ProductService;
import com.shopapi.shop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;

    private final UserService userService;

    private final ProductService productService;

    @Override
    public List<FavouriteResponseDTO> getFavouritesByUser(long userId) {
        List<Favourite> favourites = favouriteRepository.findAllByUser_Id(userId);
        return favourites.stream()
                .map(favourite -> new FavouriteResponseDTO(userId,
                        favourite.getProduct().getId(),
                        favourite.getId()))
                .toList();
    }

    @Override
    public FavouriteResponseDTO getFavouriteById(long id) {
        Favourite favourite = favouriteRepository.findById(id).orElse(null);
        //todo проверки
        return new FavouriteResponseDTO(id, favourite.getUser().getId(), favourite.getProduct().getId());
    }

    @Override
    public void addFavourite(FavouriteRequestDTO favouriteRequestDTO) {
        User user = userService.getById(favouriteRequestDTO.getUserId());
        Product product = productService.getById(favouriteRequestDTO.getProductId());
        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setProduct(product);
        favouriteRepository.save(favourite);
    }

    @Override
    public void deleteFavourite(FavouriteRequestDTO favouriteRequestDTO) {
        favouriteRepository.deleteByUserAndProduct(favouriteRequestDTO.getUserId(), favouriteRequestDTO.getProductId());
    }
}

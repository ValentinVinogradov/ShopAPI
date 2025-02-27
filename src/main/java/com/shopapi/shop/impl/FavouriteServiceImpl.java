package com.shopapi.shop.impl;

import com.shopapi.shop.dto.FavouriteRequestDTO;
import com.shopapi.shop.models.Favourite;
import com.shopapi.shop.dto.FavouriteResponseDTO;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.FavouriteRepository;
import com.shopapi.shop.repositories.UserRepository;
import com.shopapi.shop.services.FavouriteService;
import com.shopapi.shop.services.ProductService;
import com.shopapi.shop.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;

    public FavouriteServiceImpl(FavouriteRepository favouriteRepository,
                                ProductServiceImpl productService,
                                UserServiceImpl userService) {
        this.favouriteRepository = favouriteRepository;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public List<FavouriteResponseDTO> getFavouritesByUserId(UUID userId) {
        List<Favourite> favourites = favouriteRepository.findAllByUser_Id(userId);
        if (favourites != null) {
            return favourites.stream()
                    .map(favourite -> new FavouriteResponseDTO(
                            userId,
                            favourite.getProduct().getId()))
                    .toList();
        } else {
            throw new EntityNotFoundException("Favourites not found");
        }
    }

    @Override
    public void addFavourite(FavouriteRequestDTO favouriteRequestDTO) {
        User user = userService.getById(favouriteRequestDTO.userId());
        Product product = productService.getProductById(favouriteRequestDTO.productId());
        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setProduct(product);
        favouriteRepository.save(favourite);
    }

    @Override
    public void deleteFavouriteById(UUID userId, UUID productId) {
        favouriteRepository.deleteByUser_IdAndProduct_Id(userId, productId);
    }


}

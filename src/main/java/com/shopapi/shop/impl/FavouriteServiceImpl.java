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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;

    private final UserRepository userRepository;

    private final ProductService productService;

    public FavouriteServiceImpl(FavouriteRepository favouriteRepository, UserRepository userRepository, ProductService productService) {
        this.favouriteRepository = favouriteRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    @Override
    public FavouriteResponseDTO getFavouriteById(long id) {
        Favourite favourite = favouriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Favourite not found"));
        return new FavouriteResponseDTO(id, favourite.getUser().getId(), favourite.getProduct().getId());
    }

    @Override
    public List<FavouriteResponseDTO> getFavouritesByUserId(long userId) {
        List<Favourite> favourites = favouriteRepository.findAllByUser_Id(userId);
        if (favourites != null) {
            return favourites.stream()
                    .map(favourite -> new FavouriteResponseDTO(userId,
                            favourite.getProduct().getId(),
                            favourite.getId()))
                    .toList();
        } else {
            throw new EntityNotFoundException("Favourites not found");
        }
    }

    public int getAllFavouritesByProductId(long productId) {
        return favouriteRepository.findAllByProduct_Id(productId);
    }

    @Override
    public void addFavourite(FavouriteRequestDTO favouriteRequestDTO) {
        User user = userRepository.findById(favouriteRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = productService.getById(favouriteRequestDTO.getProductId());
        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setProduct(product);
        favouriteRepository.save(favourite);
    }

    @Override
    public void deleteFavouriteById(long favouriteId) {
        favouriteRepository.deleteById(favouriteId);
//        favouriteRepository.deleteByUserAndProduct(favouriteRequestDTO.getUserId(), favouriteRequestDTO.getProductId());
    }


}

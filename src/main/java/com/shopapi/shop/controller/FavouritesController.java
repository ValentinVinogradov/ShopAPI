package com.shopapi.shop.controller;

import com.shopapi.shop.dto.FavouriteRequestDTO;
import com.shopapi.shop.impl.FavouriteServiceImpl;
import com.shopapi.shop.dto.FavouriteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/favourites")
@RequiredArgsConstructor
public class FavouritesController {

    private final FavouriteServiceImpl favouriteService;

    @GetMapping("/{favouriteId}")
    public ResponseEntity<FavouriteResponseDTO> getById(@PathVariable long favouriteId) {
        FavouriteResponseDTO favouriteResponseDTO = favouriteService.getFavouriteById(favouriteId);
        return ResponseEntity.status(HttpStatus.OK).body(favouriteResponseDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavouriteResponseDTO>> getFavouritesByUserId(@PathVariable long userId) {
        List<FavouriteResponseDTO> favouriteResponseDTOs = favouriteService.getFavouritesByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(favouriteResponseDTOs);
    }

    @PostMapping("/")
    public ResponseEntity<String> addFavourite(@RequestBody FavouriteRequestDTO favouriteRequestDTO) {
        favouriteService.addFavourite(favouriteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Favourite added successfully!");
    }

    @DeleteMapping("/{favouriteId}")
    public ResponseEntity<String> deleteFavourite(@PathVariable long favouriteId) {
        favouriteService.deleteFavouriteById(favouriteId);
        return ResponseEntity.status(HttpStatus.OK).body("Favourite deleted successfully!");
    }
}

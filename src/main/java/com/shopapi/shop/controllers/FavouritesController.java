package com.shopapi.shop.controllers;

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
    public ResponseEntity<FavouriteResponseDTO> getFavouriteById(@PathVariable long favouriteId) {
        try {
            FavouriteResponseDTO favouriteResponseDTO = favouriteService.getFavouriteById(favouriteId);
            return ResponseEntity.ok(favouriteResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavouriteResponseDTO>> getFavouritesByUserId(@PathVariable long userId) {
        try {
            List<FavouriteResponseDTO> favouriteResponseDTOs = favouriteService.getFavouritesByUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body(favouriteResponseDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //todo при создании записи о новом избранном надо вернуть кол-во избранных по товару
    @PostMapping("/")
    public ResponseEntity<String> addFavourite(@RequestBody FavouriteRequestDTO favouriteRequestDTO) {
        try {
            favouriteService.addFavourite(favouriteRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Favourite added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add favourite: " + e.getMessage());
        }
    }

    @DeleteMapping("/{favouriteId}")
    public ResponseEntity<String> deleteFavourite(@PathVariable long favouriteId) {
        try {
            favouriteService.deleteFavouriteById(favouriteId);
            return ResponseEntity.status(HttpStatus.OK).body("Favourite deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete favourite: " + e.getMessage());
        }
    }
}

package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.FavouriteRequestDTO;
import com.shopapi.shop.impl.FavouriteServiceImpl;
import com.shopapi.shop.dto.FavouriteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/favourites")
public class FavouritesController {

    private final FavouriteServiceImpl favouriteService;

    public FavouritesController(FavouriteServiceImpl favouriteService) {
        this.favouriteService = favouriteService;
    }

    @GetMapping("/{favouriteId}")
    public ResponseEntity<FavouriteResponseDTO> getFavourite(@PathVariable long favouriteId) {
        try {
            FavouriteResponseDTO favouriteResponseDTO = favouriteService.getFavouriteById(favouriteId);
            return ResponseEntity.ok(favouriteResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavouriteResponseDTO>> getFavouritesByUser(@PathVariable long userId) {
        try {
            List<FavouriteResponseDTO> favouriteResponseDTOs = favouriteService.getFavouritesByUserId(userId);
            return ResponseEntity.ok(favouriteResponseDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Integer> getAllFavouritesByProduct(@PathVariable long productId) {
        try {
            Integer countOfFavourites = favouriteService.getAllFavouritesByProductId(productId);
            return ResponseEntity.ok(countOfFavourites);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //todo при создании записи о новом избранном надо вернуть кол-во избранных по товару
    @PostMapping("/add")
    public ResponseEntity<String> addFavourite(@RequestBody FavouriteRequestDTO favouriteRequestDTO) {
        try {
            favouriteService.addFavourite(favouriteRequestDTO);
            URI uri = new URI("/shop_api/v1/favourites");
            return ResponseEntity.created(uri).body("Favourite added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add favourite: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{favouriteId}")
    public ResponseEntity<String> deleteFavourite(@PathVariable long favouriteId) {
        try {
            favouriteService.deleteFavouriteById(favouriteId);
            return ResponseEntity.ok("Favourite deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete favourite: " + e.getMessage());
        }
    }
}

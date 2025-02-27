package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.FavouriteRequestDTO;
import com.shopapi.shop.impl.FavouriteServiceImpl;
import com.shopapi.shop.dto.FavouriteResponseDTO;
import com.shopapi.shop.models.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shop_api/v1/favourites")
public class FavouritesController {

    private final FavouriteServiceImpl favouriteService;

    public FavouritesController(FavouriteServiceImpl favouriteService) {
        this.favouriteService = favouriteService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<FavouriteResponseDTO>> getFavouritesByUser(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        try {
            List<FavouriteResponseDTO> favouriteResponseDTOs = favouriteService.
                    getFavouritesByUserId(principal.getId());
            return ResponseEntity.ok(favouriteResponseDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //todo при создании записи о новом избранном надо вернуть кол-во избранных по товару
    // вроде как не надо
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

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteFavourite(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID productId) {
        try {
            favouriteService.deleteFavouriteById(principal.getId(), productId);
            return ResponseEntity.ok("Favourite deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete favourite: " + e.getMessage());
        }
    }
}

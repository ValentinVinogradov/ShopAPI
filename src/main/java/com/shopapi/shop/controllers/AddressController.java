package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.AddressRequestDTO;
import com.shopapi.shop.impl.AddressServiceImpl;
import com.shopapi.shop.models.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shop_api/v1/addresses")
public class AddressController {
    private final AddressServiceImpl addressService;

    public AddressController(AddressServiceImpl addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/get-active")
    public ResponseEntity<String> getLastAddedUserAddress(
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            return ResponseEntity.ok(addressService.getActiveAddedUserAddress(principal.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody AddressRequestDTO addressRequestDTO) {
        try {
            addressService.addAddress(principal.getId(), addressRequestDTO);
            return ResponseEntity.ok("Address added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add address");
        }
    }

    @PostMapping("/change-active/{activeAddressId}")
    public ResponseEntity<String> changeActiveAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID activeAddressId
    ) {
        try {
            addressService.changeActiveAddress(principal.getId(), activeAddressId);
            return ResponseEntity.ok("Address successfully changed to active!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to change activity of address");
        }
    }

    //todo где то тут сделать метод переключающий новый адрес
    @PostMapping("/update/{addressId}")
    public ResponseEntity<String> updateAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody AddressRequestDTO addressRequestDTO,
            @PathVariable UUID addressId) {
        try {
            addressService.updateAddress(principal.getId(), addressId, addressRequestDTO);
            return ResponseEntity.ok("Address updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update address");
        }
    }

    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<String> deleteAddressById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID addressId) {
        try {
            addressService.deleteAddressById(principal.getId(), addressId);
            return ResponseEntity.ok("Address deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete address");
        }
    }
}

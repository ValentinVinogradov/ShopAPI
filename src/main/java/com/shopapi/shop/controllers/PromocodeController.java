package com.shopapi.shop.controllers;

import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.impl.PromocodeServiceImpl;
import com.shopapi.shop.models.Promocode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop_api/v1/promocodes")
public class PromocodeController extends GenericController<Promocode, Long>{
    private final PromocodeServiceImpl promocodeService;

    public PromocodeController(PromocodeServiceImpl promocodeService) {
        super(promocodeService);
        this.promocodeService = promocodeService;
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Promocode> getPromocodeByCode(@PathVariable String code) {
        try {
            return ResponseEntity.ok(promocodeService.getPromocodeByCode(code));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> addPromocode(@RequestBody Promocode promocode) {
        try {
            promocodeService.addPromocode(promocode);
            return ResponseEntity.status(HttpStatus.CREATED).body("Promocode added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add promocode: " + e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<String> updatePromocode(@RequestBody Promocode promocode) {
        try {
            promocodeService.updatePromocode(promocode);
            return ResponseEntity.ok("Promocode update successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update promocode: " + e.getMessage());
        }
    }


    @PostMapping("/validate")
    public ResponseEntity<PromoCodeValidationStatus> validatePromocode(@RequestParam String code) {
        return ResponseEntity.ok(promocodeService.validatePromocode(code));
    }

}

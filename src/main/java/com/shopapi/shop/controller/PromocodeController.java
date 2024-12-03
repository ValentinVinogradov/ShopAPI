package com.shopapi.shop.controller;

import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Promocode;
import com.shopapi.shop.services.PromocodeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/shop_api/v1/promocodes")
public class PromocodeController {

    private final PromocodeService promocodeService;

    @PostMapping("/add_promocode")
    public ResponseEntity<Promocode> createPromocode(@RequestBody Promocode promocode) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promocodeService.createPromocode(promocode));
    }

    @GetMapping("/")
    public ResponseEntity<List<Promocode>> getAllPromocodes() {
        return ResponseEntity.ok(promocodeService.getAllPromocodes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promocode> getPromocodeById(@PathVariable Long id) {
        return ResponseEntity.ok(promocodeService.getPromocodeById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Promocode> getPromocodeByCode(@PathVariable String code) {
        return ResponseEntity.ok(promocodeService.getPromocodeByCode(code));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promocode> updatePromocode(@PathVariable Long id, @RequestBody Promocode promocode) {
        return ResponseEntity.ok(promocodeService.updatePromocode(promocode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromocode(@PathVariable Long id) {
        promocodeService.deletePromocode(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<PromoCodeValidationStatus> validatePromocode(@RequestParam String code) {
        return ResponseEntity.ok(promocodeService.validatePromocode(code));
    }

}

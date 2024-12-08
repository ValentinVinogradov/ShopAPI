package com.shopapi.shop.controller;

import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.impl.PromocodeServiceImpl;
import com.shopapi.shop.models.Promocode;
import com.shopapi.shop.services.PromocodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/promocodes")
public class PromocodeController extends GenericController<Promocode, Long>{
    private final PromocodeServiceImpl promocodeService;

    public PromocodeController(PromocodeServiceImpl promocodeService) {
        super(promocodeService);
        this.promocodeService = promocodeService;
    }

    @PostMapping("/")
    public ResponseEntity<String> addPromocode(@RequestBody Promocode promocode) {
        promocodeService.addPromocode(promocode);
        return ResponseEntity.status(HttpStatus.CREATED).body("Promocode added successfully!");
    }

    @PutMapping("/")
    public ResponseEntity<String> updatePromocode(@RequestBody Promocode promocode) {
        promocodeService.updatePromocode(promocode);
        return ResponseEntity.ok("Promocode update successfully!");
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Promocode> getPromocodeByCode(@PathVariable String code) {
        return ResponseEntity.ok(promocodeService.getPromocodeByCode(code));
    }

    @PostMapping("/validate")
    public ResponseEntity<PromoCodeValidationStatus> validatePromocode(@RequestParam String code) {
        return ResponseEntity.ok(promocodeService.validatePromocode(code));
    }

}

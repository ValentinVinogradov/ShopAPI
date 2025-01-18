package com.shopapi.shop.controllers;


import com.shopapi.shop.impl.ProductServiceImpl;
import com.shopapi.shop.models.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/products")
public class ProductController extends GenericController<Product, Long> {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        super(productService);
        this.productService = productService;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        try {
            Product product = productService.getProductByName(name);
            return ResponseEntity.ok(product); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        try {
            List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(products); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // 404 Not Found
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        try {
            productService.addProduct(product);
            URI uri = new URI("/shop_api/v1/products");
            return ResponseEntity.created(uri).body("Product added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add product: " + e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
        try {
            productService.updateProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update product: " + e.getMessage());
        }
    }

    @PatchMapping("/{productId}/change_price")
    public ResponseEntity<String> updateProductPrice(@PathVariable Long productId, @RequestBody BigDecimal newPrice) {
        try {
            productService.updateProductPrice(productId, newPrice);
            return ResponseEntity.ok("Price changed to " + newPrice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update product price: " + e.getMessage());
        }
    }

    @PutMapping("/{productId}/apply_discount")
    public ResponseEntity<String> applyDiscount(@PathVariable long productId, @RequestBody int discountPercentage) {
        try {
            productService.applyDiscount(productId, discountPercentage);
            return ResponseEntity.ok("Discount successfully applied: " + discountPercentage + "%");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to apply discount for product: " + e.getMessage());
        }
    }
}

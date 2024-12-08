package com.shopapi.shop.controller;


import com.shopapi.shop.impl.ProductServiceImpl;
import com.shopapi.shop.models.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/products")
public class ProductController extends GenericController<Product, Long> {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        super(productService);
        this.productService = productService;
    }

    @PostMapping("/")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully!");
    }

    @PutMapping("/")
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product updated successfully!");
    }

    @PatchMapping("/{productId}/change_price")
    public ResponseEntity<String> updateProductPrice(@PathVariable Long productId, @RequestBody BigDecimal newPrice) {
        productService.updateProductPrice(productId, newPrice);
        return ResponseEntity.ok("Price changed to " + newPrice);
    }

    @PutMapping("/{productId}/apply_discount")
    public ResponseEntity<String> applyDiscount(@PathVariable long productId, @RequestBody int discountPercentage) {
        productService.applyDiscount(productId, discountPercentage);
        return ResponseEntity.ok("Discount successfully applied: " + discountPercentage + "%");
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        if (product != null) {
            return ResponseEntity.ok(product); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // 404 Not Found
        }
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        if (!products.isEmpty()) {
            return ResponseEntity.ok(products); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // 404 Not Found
        }
    }
}

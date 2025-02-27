package com.shopapi.shop.controllers;


import com.shopapi.shop.dto.ProductCatalogDTO;
import com.shopapi.shop.dto.ProductSaveDTO;
import com.shopapi.shop.impl.ProductServiceImpl;
import com.shopapi.shop.models.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;


//todo подумать над реализацией гет и пост запросов через дто как то (ниче страшного как выяснилось)
@RestController
@RequestMapping("/shop_api/v1/products")
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductCatalogDTO> getProductById(@PathVariable UUID productId) {
        try {
            return ResponseEntity.ok(productService.getProductOfCatalog(productId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //todo подумать над тем чтобы возвращать какую то часть товара для каталога, а при заходе возвращать фул инфу
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            return ResponseEntity.ok(productService.getAll());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductSaveDTO productDTO) {
        try {
            productService.addProduct(productDTO);
            URI uri = new URI("/shop_api/v1/products");
            return ResponseEntity.created(uri).body("Product added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add product: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductSaveDTO productDTO) {
        try {
            productService.updateProduct(productId, productDTO);
            URI uri = new URI("/shop_api/v1/products");
            return ResponseEntity.created(uri).body("Product updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update product: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PatchMapping("/{productId}/change_price")
    public ResponseEntity<String> updateProductPrice(@PathVariable UUID productId, @RequestBody BigDecimal newPrice) {
        try {
            productService.updateProductPrice(productId, newPrice);
            return ResponseEntity.ok("Price changed to " + newPrice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update product price: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{productId}/apply_discount")
    public ResponseEntity<String> applyDiscount(@PathVariable UUID productId, @RequestBody int discountPercentage) {
        try {
            productService.applyDiscount(productId, discountPercentage);
            return ResponseEntity.ok("Discount successfully applied: " + discountPercentage + "%");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to apply discount for product: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable UUID productId) {
        try {
            productService.deleteById(productId);
            return ResponseEntity.ok("Product deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete product");
        }
    }
}

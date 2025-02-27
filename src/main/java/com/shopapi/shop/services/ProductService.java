package com.shopapi.shop.services;

import com.shopapi.shop.dto.ProductSaveDTO;
import com.shopapi.shop.models.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    List<Product> getAll();
    Product getProductById(UUID productId);
    void deleteById(UUID productId);
    Product getProductByName(String name);                           // Получить продукт по имени
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice); // Получить продукты по диапазону цен
    void addProduct(ProductSaveDTO productDTO);
    void updateProduct(UUID productId, ProductSaveDTO productDTO);
    void updateProductPrice(UUID productId, BigDecimal newPrice);
    void applyDiscount(UUID productId, int discountPercentage);
}

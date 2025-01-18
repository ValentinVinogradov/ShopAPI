package com.shopapi.shop.services;

import com.shopapi.shop.models.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService extends GenericService<Product, Long> {
    Product getProductByName(String name);                           // Получить продукт по имени
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice); // Получить продукты по диапазону цен
    void addProduct(Product product);
    void updateProduct(Product product);
    void updateProductPrice(Long productId, BigDecimal newPrice);
    void applyDiscount(Long productId, int discountPercentage);
}

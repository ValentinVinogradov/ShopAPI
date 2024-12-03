package com.shopapi.shop.services;

import com.shopapi.shop.models.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService extends GenericService<Product, Long>{
    //todo допилить эти три метода
    Product getProductByName(String name);                           // Получить продукт по имени
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice); // Получить продукты по диапазону цен
}

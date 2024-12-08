package com.shopapi.shop.impl;

import com.shopapi.shop.models.Product;
import com.shopapi.shop.repository.ProductRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.ProductService;
import com.shopapi.shop.utils.PriceUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import com.shopapi.shop.utils.DateUtils;



@Service
public class ProductServiceImpl extends AbstractService<Product, Long> implements ProductService{

    public ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    private void setDate(Product product) {
        product.setLastDate(DateUtils.getCurrentDate());
        productRepository.save(product);
    }



    @Transactional
    @Override
    public void updateProductPrice(Long productId, BigDecimal newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setCurrentPrice(newPrice);
        setDate(product);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void applyDiscount(Long productId, int discountPercentage) {
        // Получаем продукт из базы данных
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Сохраняем старую цену
        product.setOldPrice(product.getCurrentPrice());

        // Рассчитываем новую цену с учетом скидки
        BigDecimal discountedPrice = PriceUtils.calculateDiscountedPrice(product.getCurrentPrice(), discountPercentage);
        product.setCurrentPrice(discountedPrice);

        // Обновляем дату изменения
        setDate(product);

        // Сохраняем изменения в базу данных
        productRepository.save(product);
    }

    @Override
    public Product getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return List.of();
    }
}

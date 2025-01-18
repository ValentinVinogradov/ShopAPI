package com.shopapi.shop.impl;

import com.shopapi.shop.models.Product;
import com.shopapi.shop.repositories.ProductRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.ProductService;
import com.shopapi.shop.utils.PriceUtils;
import jakarta.persistence.EntityNotFoundException;
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
    public Product getProductByName(String name) {
        return productRepository.findProductByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Transactional
    @Override
    public void addProduct(Product product) {
        setDate(product);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void updateProduct(Product product) {
        setDate(product);
        productRepository.save(product);
    }

    private void setDate(Product product) {
        product.setLastDate(DateUtils.getCurrentDate());
    }

    @Transactional
    @Override
    public void updateProductPrice(Long productId, BigDecimal newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setPrice(newPrice);
        setDate(product);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void applyDiscount(Long productId, int discountPercentage) {
        // Получаем продукт из базы данных
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Сохраняем старую цену
        product.setOldPrice(product.getPrice());

        // Рассчитываем новую цену с учетом скидки
        BigDecimal discountedPrice = PriceUtils.calculateDiscountedPrice(product.getPrice(), discountPercentage);
        product.setPrice(discountedPrice);

        // Обновляем дату изменения
        setDate(product);

        // Сохраняем изменения в базу данных
        productRepository.save(product);
    }

    //todo надо ли?
    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return List.of();
    }
}

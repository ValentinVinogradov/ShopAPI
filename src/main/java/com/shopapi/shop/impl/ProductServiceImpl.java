package com.shopapi.shop.impl;

import com.shopapi.shop.dto.ProductCatalogDTO;
import com.shopapi.shop.dto.ProductSaveDTO;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.repositories.ProductRepository;
import com.shopapi.shop.services.ProductService;
import com.shopapi.shop.utils.PriceUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.shopapi.shop.utils.DateUtils;



@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductByName(String name) {
        return productRepository.findProductByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Transactional
    @Override
    public void addProduct(ProductSaveDTO productDTO) {
        Product product = new Product();
        if (!(productDTO.groupId() == null)) {
            product.setGroupId(productDTO.groupId());
        } else {
            product.setGroupId(UUID.randomUUID());
        }
        product.setName(productDTO.name());
        product.setImg(productDTO.img());
        product.setCategory(productDTO.category());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price());
        product.setAttributes(productDTO.attributes());
        product.setStockQuantity(productDTO.stockQuantity());
        product.setReviewCount(0);

        setDate(product);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void updateProduct(UUID productId, ProductSaveDTO productDTO) {
        Product product = getProductById(productId);

        if (productDTO.groupId() != null && !productDTO.groupId().equals(product.getGroupId())) {
            product.setGroupId(productDTO.groupId());
        }
        if (!productDTO.name().equals(product.getName())) {
            product.setName(productDTO.name());
        }
        if (!productDTO.img().equals(product.getImg())) {
            product.setImg(productDTO.img());
        }
        if (!productDTO.category().equals(product.getCategory())) {
            product.setCategory(productDTO.category());
        }
        if (!productDTO.description().equals(product.getDescription())) {
            product.setDescription(productDTO.description());
        }
        if (!productDTO.price().equals(product.getPrice())) {
            product.setPrice(productDTO.price());
        }
        if (!productDTO.attributes().equals(product.getAttributes())) {
            product.setAttributes(productDTO.attributes());
        }
        if (productDTO.stockQuantity().equals(product.getStockQuantity())) {
            product.setStockQuantity(productDTO.stockQuantity());
        }

        setDate(product);
        productRepository.save(product);
    }

    private void setDate(Product product) {
        product.setLastDate(DateUtils.getCurrentDate());
    }

    @Transactional
    @Override
    public void updateProductPrice(UUID productId, BigDecimal newPrice) {
        Product product = getProductById(productId);
        product.setPrice(newPrice);
        setDate(product);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void applyDiscount(UUID productId, int discountPercentage) {
        // Получаем продукт из базы данных
        Product product = getProductById(productId);

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

    //todo надо ли? (наверное нет)
    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return List.of();
    }

    @Override
    public List<Product> getAll() {
        return List.of();
    }

    public ProductCatalogDTO getProductOfCatalog(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return new ProductCatalogDTO(
                product.getName(),
                product.getPrice(),
                product.getOldPrice(),
                product.getImg(),
                product.getDiscountPercentage(),
                product.getReviewCount(),
                product.getRating()
        );
    }

    @Override
    public void deleteById(UUID productId) {

    }
}

package com.shopapi.shop.impl;

import com.shopapi.shop.dto.ReviewRequestDTO;
import com.shopapi.shop.dto.ReviewResponseDTO;
import com.shopapi.shop.dto.UserResponseDTO;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.models.Review;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.ProductRepository;
import com.shopapi.shop.repositories.ReviewRepository;
import com.shopapi.shop.services.ReviewService;
import com.shopapi.shop.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ProductRepository productRepository,
                             UserServiceImpl userService,
                             ProductServiceImpl productService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.productService = productService;
    }

    private ReviewResponseDTO mapReviewToDTO(Review review) {
        User user = review.getUser();
        return new ReviewResponseDTO(review.getId(),
                review.getProduct().getId(),
                new UserResponseDTO(
                        user.getId(),
                        user.getUsername()),
                review.getUsername(),
                review.getRating(),
                review.getDignities(),
                review.getFlaws(),
                review.getContent(),
                review.getDate());
    }

    private List<ReviewResponseDTO> mapListReviewsToDTO(List<Review> reviews) {
        return reviews.stream()
                .map(this::mapReviewToDTO)
                .toList();
    }

    @Override
    public ReviewResponseDTO getReviewById(long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        return mapReviewToDTO(review);
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByProductId(UUID productId) {
        List<Review> reviews = reviewRepository.findAllByProduct_Id(productId);
        return mapListReviewsToDTO(reviews);
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByUserId(UUID userId) {
        List<Review> reviews = reviewRepository.findAllByUser_Id(userId);
        return mapListReviewsToDTO(reviews);
    }

    @Transactional
    @Override
    public void addReview(UUID userId, ReviewRequestDTO reviewRequestDTO) {
        User user = userService.getById(userId);
        Product product = productService.getProductById(reviewRequestDTO.productId());
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(reviewRequestDTO.rating());
        review.setContent(reviewRequestDTO.content());
        review.setUsername(user.getUsername());
        review.setDignities(reviewRequestDTO.dignities());
        review.setFlaws(reviewRequestDTO.flaws());
        review.setDate(DateUtils.getCurrentDate());

        product.setReviewCount(product.getReviewCount() + 1);
        productRepository.save(product);
        // Сохраняем в базу

        reviewRepository.save(review);
    }

    @Transactional
    @Override
    public void updateReview(UUID userId, ReviewRequestDTO reviewRequestDTO) {
        UUID productId = reviewRequestDTO.productId();
        Review review = reviewRepository.findByUser_IdAndProduct_Id(userId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        review.setContent(reviewRequestDTO.content());
        review.setRating(reviewRequestDTO.rating());
        review.setDignities(reviewRequestDTO.dignities());
        review.setFlaws(reviewRequestDTO.flaws());
        review.setDate(DateUtils.getCurrentDate());
        reviewRepository.save(review);
    }

    @Override
    public void deleteReviewById(UUID userId, UUID productId) {
        Review review = reviewRepository.findByUser_IdAndProduct_Id(userId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        // Уменьшаем количество отзывов у товара
        Product product = review.getProduct(); // Можем получить продукт напрямую через отзыв
        Integer reviewsCount = product.getReviewCount();

        if (reviewsCount > 0) {
            product.setReviewCount(reviewsCount - 1);
            productRepository.save(product);
        } else {
            throw new IllegalStateException("Product reviews count must be > 0 to delete");
        }

        // Удаляем отзыв
        reviewRepository.delete(review);
    }
}

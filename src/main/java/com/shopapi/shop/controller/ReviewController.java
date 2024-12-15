package com.shopapi.shop.controller;

import com.shopapi.shop.dto.ReviewRequestDTO;
import com.shopapi.shop.dto.ReviewResponseDTO;
import com.shopapi.shop.impl.ReviewServiceImpl;
import com.shopapi.shop.models.Review;
import com.shopapi.shop.repository.ProductRepository;
import com.shopapi.shop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/reviews")
public class ReviewController {
    private final ReviewServiceImpl reviewService;


    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;

    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable long reviewId) {
        ReviewResponseDTO reviewResponseDTO = reviewService.getReviewById(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponseDTO);
    }

    @PostMapping("/")
    public ResponseEntity<String> addReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        reviewService.addReview(reviewRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Review added successful!");
    }

    @PutMapping("/")
    public ResponseEntity<String> update(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        reviewService.updateReview(reviewRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Review updated successful!");
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProductId(@PathVariable long productId) {
        List<ReviewResponseDTO> reviewResponseDTOs = reviewService.getReviewsByProductId(productId);

        if (reviewResponseDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 если список пуст
        }
        return ResponseEntity.ok(reviewResponseDTOs); // 200 если данные найдены
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUserId(@PathVariable long userId) {
        List<ReviewResponseDTO> reviewResponseDTOs = reviewService.getReviewsByUserId(userId);

        if (reviewResponseDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 если список пуст
        }
        return ResponseEntity.ok(reviewResponseDTOs); // 200 если данные найдены
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReviewById(@PathVariable long reviewId) {
        reviewService.deleteReviewById(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body("Review deleted successfully");
    }
}

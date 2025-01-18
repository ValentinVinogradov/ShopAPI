package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.ReviewRequestDTO;
import com.shopapi.shop.dto.ReviewResponseDTO;
import com.shopapi.shop.impl.ReviewServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/reviews")
public class ReviewController {
    private final ReviewServiceImpl reviewService;


    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable long reviewId) {
        try {
            ReviewResponseDTO reviewResponseDTO = reviewService.getReviewById(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(reviewResponseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProductId(@PathVariable long productId) {
        try {
            List<ReviewResponseDTO> reviewResponseDTOs = reviewService.getReviewsByProductId(productId);
            return ResponseEntity.ok(reviewResponseDTOs); // 200 если данные найдены
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }


    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUserId(@PathVariable long userId) {
        try {
            List<ReviewResponseDTO> reviewResponseDTOs = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.ok(reviewResponseDTOs); // 200 если данные найдены
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> addReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        try {
            reviewService.addReview(reviewRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Review added successful!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add review: " + e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<String> updateReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        try {
            reviewService.updateReview(reviewRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Review updated successful!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update review: " + e.getMessage());
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReviewById(@PathVariable long reviewId) {
        try {
            reviewService.deleteReviewById(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body("Review deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete review: " + e.getMessage());
        }
    }
}

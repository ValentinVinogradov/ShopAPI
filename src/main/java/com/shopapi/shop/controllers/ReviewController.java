package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.ReviewRequestDTO;
import com.shopapi.shop.dto.ReviewResponseDTO;
import com.shopapi.shop.impl.ReviewServiceImpl;
import com.shopapi.shop.models.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shop_api/v1/reviews")
public class ReviewController {
    private final ReviewServiceImpl reviewService;

    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable long reviewId) {
        try {
            ReviewResponseDTO reviewResponseDTO = reviewService.getReviewById(reviewId);
            return ResponseEntity.ok(reviewResponseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/product/{productId}/all")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProductId(@PathVariable UUID productId) {
        try {
            List<ReviewResponseDTO> reviewResponseDTOs = reviewService.getReviewsByProductId(productId);
            return ResponseEntity.ok(reviewResponseDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUserId(
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            List<ReviewResponseDTO> reviewResponseDTOs = reviewService.getReviewsByUserId(principal.getId());
            return ResponseEntity.ok(reviewResponseDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReview(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody ReviewRequestDTO reviewRequestDTO) {
        try {
            reviewService.addReview(principal.getId(), reviewRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Review added successful!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add review: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateReview(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody ReviewRequestDTO reviewRequestDTO) {
        try {
            reviewService.updateReview(principal.getId(), reviewRequestDTO);
            return ResponseEntity.ok("Review updated successful!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update review: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteReviewById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID productId) {
        try {
            reviewService.deleteReviewById(principal.getId(), productId);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete review: " + e.getMessage());
        }
    }
}

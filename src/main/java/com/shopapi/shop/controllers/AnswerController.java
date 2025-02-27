package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.AnswerRequestDTO;
import com.shopapi.shop.dto.AnswerResponseDTO;
import com.shopapi.shop.impl.AnswerServiceImpl;
import com.shopapi.shop.models.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/shop_api/v1/answers")
public class AnswerController{

    private final AnswerServiceImpl answerService;

    public AnswerController(AnswerServiceImpl answerService) {
        this.answerService = answerService;
    }


    @GetMapping("/{answerId}")
    public ResponseEntity<AnswerResponseDTO> getAnswerById(@PathVariable long answerId) {
        try {
            AnswerResponseDTO answerResponseDTO = answerService.getAnswerById(answerId);
            return ResponseEntity.ok(answerResponseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<AnswerResponseDTO>> getAnswersByUserId(
            @AuthenticationPrincipal UserPrincipal principal) {
        try{
            List<AnswerResponseDTO> answerResponseDTOs = answerService.getAnswersByUserId(principal.getId());
            return ResponseEntity.ok(answerResponseDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PostMapping("/add")
    public ResponseEntity<String> addAnswer(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody AnswerRequestDTO answerRequestDTO) {
        try {
            answerService.addAnswer(principal.getId(), answerRequestDTO);
            URI uri = new URI("/shop_api/v1/answers");
            return ResponseEntity.created(uri).body("Answer added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add answer: " + e.getMessage());
        }

    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAnswer(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody AnswerRequestDTO answerRequestDTO) {
        try {
            answerService.updateAnswer(principal.getId(), answerRequestDTO);
            return ResponseEntity.ok("Answer updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update answer: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<String> deleteAnswerById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable long questionId) {
        try {
            answerService.deleteAnswerById(principal.getId(), questionId);
            return ResponseEntity.ok("Answer deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete answer: " + e.getMessage());
        }
    }
}

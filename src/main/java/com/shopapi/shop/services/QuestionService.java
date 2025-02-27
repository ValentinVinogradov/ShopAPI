package com.shopapi.shop.services;

import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.dto.QuestionResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface QuestionService {
    QuestionResponseDTO getQuestionById(long questionId);
    List<QuestionResponseDTO> getQuestionsByProductId(UUID productId);
    List<QuestionResponseDTO> getQuestionsByUserId(UUID userId);
    void addQuestion(UUID userId, QuestionRequestDTO questionRequestDTO);
    void updateQuestion(UUID userId, QuestionRequestDTO questionRequestDTO);
    void deleteQuestionById(UUID userId, UUID productId);
}

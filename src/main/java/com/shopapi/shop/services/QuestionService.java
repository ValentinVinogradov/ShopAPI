package com.shopapi.shop.services;

import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.dto.QuestionResponseDTO;

import java.util.List;

public interface QuestionService {
    QuestionResponseDTO getQuestionById(long questionId);
    List<QuestionResponseDTO> getQuestionsByProductId(long productId);
    List<QuestionResponseDTO> getQuestionsByUserId(long userId);
    void addQuestion(QuestionRequestDTO questionRequestDTO);
    void updateQuestion(QuestionRequestDTO questionRequestDTO);
    void deleteQuestionById(long questionId);
}

package com.shopapi.shop.services;

import com.shopapi.shop.dto.AnswerRequestDTO;
import com.shopapi.shop.dto.AnswerResponseDTO;
import com.shopapi.shop.models.Answer;

import java.util.List;
import java.util.UUID;

public interface AnswerService {
    AnswerResponseDTO getAnswerById(long answerId);
    List<AnswerResponseDTO> getAnswersByUserId(UUID userId);
    void addAnswer(UUID userId, AnswerRequestDTO answerRequestDTO);
    void updateAnswer(UUID userId, AnswerRequestDTO answerRequestDTO);
    void deleteAnswerById(UUID userId, long questionId);
}

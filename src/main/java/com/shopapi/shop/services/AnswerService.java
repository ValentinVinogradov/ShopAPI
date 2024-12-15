package com.shopapi.shop.services;

import com.shopapi.shop.dto.AnswerRequestDTO;
import com.shopapi.shop.dto.AnswerResponseDTO;
import com.shopapi.shop.models.Answer;

import java.util.List;

public interface AnswerService {
    AnswerResponseDTO getAnswerById(long answerId);
    List<AnswerResponseDTO> getAnswersByUserId(long userId);
    void addAnswer(AnswerRequestDTO answerRequestDTO);
    void updateAnswer(AnswerRequestDTO answerRequestDTO);
    void deleteAnswerById(long answerId);
}

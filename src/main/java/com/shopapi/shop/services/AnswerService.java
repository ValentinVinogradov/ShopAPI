package com.shopapi.shop.services;

import com.shopapi.shop.dto.AnswerRequestDTO;
import com.shopapi.shop.models.Answer;

import java.util.List;

public interface AnswerService {
    void addAnswer(AnswerRequestDTO answerRequestDTO);
    void updateAnswer(AnswerRequestDTO answerRequestDTO);
    List<Answer> getAnswersByQuestionId(long questionId);
}

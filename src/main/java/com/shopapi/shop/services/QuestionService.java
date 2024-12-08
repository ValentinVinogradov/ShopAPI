package com.shopapi.shop.services;

import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.models.Answer;
import com.shopapi.shop.models.Question;

import java.util.List;

public interface QuestionService {
    void addQuestion(QuestionRequestDTO questionRequestDTO);
    void updateQuestion(QuestionRequestDTO questionRequestDTO);
    List<Question> getQuestionsByProductId(long productId);
    List<Question> getQuestionsByUserId(long userId);
    List<Answer> getAnswersByQuestionId(long questionId);
}

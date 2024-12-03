package com.shopapi.shop.services;

import com.shopapi.shop.models.Answer;

import java.util.List;

public interface AnswerService {
    List<Answer> getAnswersByQuestionId(long questionId);
}

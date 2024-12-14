package com.shopapi.shop.dto;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.shopapi.shop.models.Question}
 */
@Value
public class QuestionResponseDTO {

    //todo при getAll возвращать последние два ответа на вопрос
    Long id;
    UserResponseDTO userResponseDTO;
    String username;
    String content;
    LocalDate date;
    List<AnswerResponseDTO> answers;
}
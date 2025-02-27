package com.shopapi.shop.impl;

import com.shopapi.shop.dto.AnswerRequestDTO;
import com.shopapi.shop.dto.AnswerResponseDTO;
import com.shopapi.shop.dto.UserResponseDTO;
import com.shopapi.shop.models.Answer;
import com.shopapi.shop.models.Question;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.AnswerRepository;
import com.shopapi.shop.repositories.QuestionRepository;
import com.shopapi.shop.services.AnswerService;
import com.shopapi.shop.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final UserServiceImpl userService;
    private final QuestionRepository questionRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository,
                             UserServiceImpl userService,
                             QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.userService = userService;
        this.questionRepository = questionRepository;
    }

    public AnswerResponseDTO mapAnswerToDTO(Answer answer) {
        User user = answer.getUser();
        return new AnswerResponseDTO(
                answer.getId(),
                new UserResponseDTO(
                        user.getId(),
                        user.getUsername()),
                answer.getUsername(),
                answer.getContent(),
                answer.getDate());
    }

    private List<AnswerResponseDTO> mapListAnswersToDTO(List<Answer> answers) {
        return answers.stream()
                .map(this::mapAnswerToDTO)
                .toList();
    }

    @Override
    public AnswerResponseDTO getAnswerById(long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found"));
        return mapAnswerToDTO(answer);
    }

    @Override
    public List<AnswerResponseDTO> getAnswersByUserId(UUID userId) {
        List<Answer> answers = answerRepository.findAllByUser_Id(userId);
        return mapListAnswersToDTO(answers);
    }

    //todo по моему не надо
//    @Override
//    public List<Answer> getAnswersByQuestionId(long questionId) {
//        return answerRepository.findAnswersByQuestionId(questionId);
//    }


    //todo надо ли подгружать вопрос из ответа? ну я про lazy
    @Transactional
    @Override
    public void addAnswer(UUID userId, AnswerRequestDTO answerRequestDTO) {
        User user = userService.getById(userId);
        Question question = questionRepository.findById((answerRequestDTO.questionId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        Answer answer = new Answer();
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setUsername(user.getUsername());
        answer.setContent(answerRequestDTO.content());
        answer.setDate(DateUtils.getCurrentDate());
        answerRepository.save(answer);
    }

    @Transactional
    @Override
    public void updateAnswer(UUID userId, AnswerRequestDTO answerRequestDTO) {
        Answer exsistingAnswer = answerRepository.
                findAllByUser_IdAndQuestion_Id(userId, answerRequestDTO.questionId())
                .orElseThrow(() -> new EntityNotFoundException("Answer not found"));
        exsistingAnswer.setContent(answerRequestDTO.content());
        exsistingAnswer.setDate(DateUtils.getCurrentDate());
        answerRepository.save(exsistingAnswer);
    }

    @Override
    public void deleteAnswerById(UUID userId, long questionId) {
        answerRepository.deleteAllByUser_IdAndQuestion_Id(userId, questionId);
    }
}

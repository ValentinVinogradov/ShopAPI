package com.shopapi.shop.impl;

import com.shopapi.shop.dto.AnswerResponseDTO;
import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.dto.QuestionResponseDTO;
import com.shopapi.shop.dto.UserResponseDTO;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.models.Question;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.QuestionRepository;
import com.shopapi.shop.services.QuestionService;
import com.shopapi.shop.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;
    private final AnswerServiceImpl answerService;

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               UserServiceImpl userService,
                               ProductServiceImpl productService,
                               AnswerServiceImpl answerService) {
        this.questionRepository = questionRepository;
        this.userService = userService;
        this.productService = productService;
        this.answerService = answerService;
    }

    private QuestionResponseDTO mapQuestionToDTO(Question question) {
        User user = question.getUser();
        return new QuestionResponseDTO(
                question.getId(),
                question.getProduct().getId(),
                new UserResponseDTO(
                        user.getId(),
                        user.getUsername()),
                question.getUsername(),
                question.getContent(),
                question.getDate(),
                question.getAnswers().stream()
                        .map(answerService::mapAnswerToDTO)
                        .toList());
    }

    private List<QuestionResponseDTO> mapListQuestionsToDTO(List<Question> questions) {
        return questions.stream()
                .map(this::mapQuestionToDTO)
                .toList();
    }

    @Override
    public QuestionResponseDTO getQuestionById(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with ID: " + questionId));
        return mapQuestionToDTO(question);
    }

    @Override
    public List<QuestionResponseDTO> getQuestionsByProductId(UUID productId) {
        List<Question> questions = questionRepository.findAllByProduct_Id(productId);
        return mapListQuestionsToDTO(questions);
    }

    @Override
    public List<QuestionResponseDTO> getQuestionsByUserId(UUID userId) {
        List<Question> questions = questionRepository.findAllByUser_Id(userId);
        return mapListQuestionsToDTO(questions);
    }

    @Transactional
    @Override
    public void addQuestion(UUID userId, QuestionRequestDTO questionRequestDTO) {
        User user = userService.getById(userId);
        Product product = productService.getProductById(questionRequestDTO.productId());
        Question question = new Question();
        question.setUser(user);
        question.setProduct(product);
        question.setContent(questionRequestDTO.content());
        question.setUsername(user.getUsername());
        question.setDate(DateUtils.getCurrentDate());
        questionRepository.save(question);
    }

    @Transactional
    @Override
    public void updateQuestion(UUID userId, QuestionRequestDTO questionRequestDTO) {
        Question exsistingQuestion = questionRepository.
                findByUser_IdAndProduct_Id(userId, questionRequestDTO.productId())
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        exsistingQuestion.setContent(questionRequestDTO.content());
        exsistingQuestion.setDate(DateUtils.getCurrentDate());
        questionRepository.save(exsistingQuestion);
    }

    @Transactional
    @Override
    public void deleteQuestionById(UUID userId, UUID productId) {
        questionRepository.deleteByUser_IdAndProduct_Id(userId, productId);
    }


}

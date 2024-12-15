package com.shopapi.shop.impl;

import com.shopapi.shop.dto.AnswerResponseDTO;
import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.dto.QuestionResponseDTO;
import com.shopapi.shop.dto.UserResponseDTO;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.models.Question;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repository.ProductRepository;
import com.shopapi.shop.repository.QuestionRepository;
import com.shopapi.shop.repository.UserRepository;
import com.shopapi.shop.services.QuestionService;
import com.shopapi.shop.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               UserRepository userRepository,
                               ProductRepository productRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public QuestionResponseDTO getQuestionById(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with ID: " + questionId));
        User user = question.getUser();

        // Преобразуем ответы в DTO
        List<AnswerResponseDTO> answerDTOs = question.getAnswers().stream()
                .map(answer -> new AnswerResponseDTO(answer.getId(),
                        new UserResponseDTO(user.getId(), user.getUsername()),
                        answer.getUsername(),
                        answer.getContent(),
                        answer.getDate()))
                .toList();

        // Возвращаем вопрос и ответы в виде DTO
        return new QuestionResponseDTO(
                question.getId(),
                new UserResponseDTO(user.getId(), user.getUsername()),
                question.getUsername(),
                question.getContent(),
                question.getDate(),
                answerDTOs
        );
    }

    @Override
    public List<QuestionResponseDTO> getQuestionsByProductId(long productId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new EntityNotFoundException("Product not found"));
        List<Question> questions = questionRepository.findQuestionsByProduct_Id(productId);
        return questions.stream()
                .map(question -> new QuestionResponseDTO(question.getId(),
                        new UserResponseDTO(question.getUser().getId(), question.getUser().getUsername()),
                        question.getUsername(),
                        question.getContent(),
                        question.getDate(),
                        question.getAnswers().stream()
                                .map(answer -> new AnswerResponseDTO(answer.getId(),
                                        new UserResponseDTO(question.getUser().getId(), question.getUser().getUsername()),
                                        answer.getUsername(),
                                        answer.getContent(),
                                        answer.getDate()))
                                .toList()))
                .toList();
    }

    @Override
    public List<QuestionResponseDTO> getQuestionsByUserId(long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getUsername());
        List<Question> questions = questionRepository.findQuestionsByUser_Id(userId);
        return questions.stream()
                .map(question -> new QuestionResponseDTO(question.getId(),
                        userResponseDTO,
                        question.getUsername(),
                        question.getContent(),
                        question.getDate(),
                        question.getAnswers().stream()
                                .map(answer -> new AnswerResponseDTO(answer.getId(),
                                        userResponseDTO,
                                        answer.getUsername(),
                                        answer.getContent(),
                                        answer.getDate()))
                                .toList()))
                .toList();
    }

    @Transactional
    @Override
    public void addQuestion(QuestionRequestDTO questionRequestDTO) {
        User user = userRepository.findById(questionRequestDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = productRepository.findById(questionRequestDTO.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Question question = new Question();
        question.setUser(user);
        question.setProduct(product);
        question.setContent(questionRequestDTO.getContent());
        question.setUsername(user.getUsername());
        question.setDate(DateUtils.getCurrentDate());
        questionRepository.save(question);
    }

    @Transactional
    @Override
    public void updateQuestion(QuestionRequestDTO questionRequestDTO) {
        Long userId = questionRequestDTO.getUserId();
        Long productId = questionRequestDTO.getProductId();
        Question exsistingQuestion = questionRepository.findQuestionByUserIdAndProductId(userId, productId).orElseThrow(() -> new EntityNotFoundException("Question not found"));
        exsistingQuestion.setContent(questionRequestDTO.getContent());
        exsistingQuestion.setDate(DateUtils.getCurrentDate());
        questionRepository.save(exsistingQuestion);
    }

    @Transactional
    @Override
    public void deleteQuestionById(long questionId) {
//        Long userId = questionResponseDTO.getUser().getId();
//        Long questionId = questionResponseDTO.getId();
//        questionRepository.deleteQuestionByIdAndUserId(questionId, userId);
        questionRepository.deleteById(questionId);
    }


}

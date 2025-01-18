package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByProduct_Id(long productId);
    List<Question> findAllByUser_Id(long userId);
    Optional<Question> findQuestionByUser_IdAndProduct_Id(long userId, long productId);
}

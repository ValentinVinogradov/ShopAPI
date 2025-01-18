package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAnswersByUser_Id(long userId);
    Optional<Answer> findAnswerByUser_IdAndQuestion_Id(long userId, long questionId);
}

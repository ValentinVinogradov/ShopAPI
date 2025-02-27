package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByUser_Id(UUID userId);
    Optional<Answer> findAllByUser_IdAndQuestion_Id(UUID userId, long questionId);
    void deleteAllByUser_IdAndQuestion_Id(UUID userId, long questionId);

}

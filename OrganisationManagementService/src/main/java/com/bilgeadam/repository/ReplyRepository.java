package com.bilgeadam.repository;

import com.bilgeadam.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByQuestionId(Long questionId);
}

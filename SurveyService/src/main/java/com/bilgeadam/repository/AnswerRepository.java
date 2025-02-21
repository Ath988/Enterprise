package com.bilgeadam.repository;

import com.bilgeadam.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, String> {
	
	List<Answer> findAllByIdIn(List<String> ids);
}
package com.bilgeadam.repository;

import com.bilgeadam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, String> {
	
	List<Question> findAllByIdIn(List<String> ids);
}
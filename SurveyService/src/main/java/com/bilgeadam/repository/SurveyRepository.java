package com.bilgeadam.repository;

import com.bilgeadam.entity.Survey;
import com.bilgeadam.entity.enums.EState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, String> {
	
	List<Survey> findAllByState(EState state);
	
}
package com.bilgeadam.repository;

import com.bilgeadam.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, String> {
	List<SurveyResponse> findAllByUserId(Long userId);
	List<SurveyResponse> findAllBySurveyId(String surveyId);
}
package com.bilgeadam.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_survey_response")
public class SurveyResponse extends BaseEntity{
	Long userId;
	String surveyId;
	
	@ElementCollection
	List<String> answerIds;
	
	LocalDateTime submissionDate;
}
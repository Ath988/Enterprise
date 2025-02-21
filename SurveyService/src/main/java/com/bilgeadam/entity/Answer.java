package com.bilgeadam.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_answer")
public class Answer extends BaseEntity{
	String questionId;
	String surveyResponseId;
	String answerText;
	String selectedOptionId;
}
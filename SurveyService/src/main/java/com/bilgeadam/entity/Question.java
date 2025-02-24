package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EQuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_question")
public class Question extends BaseEntity{
	String questionText;
	String surveyId;
	@Enumerated(EnumType.STRING)
	EQuestionType questionType;
	@ElementCollection
	List<String> optionIds;
}
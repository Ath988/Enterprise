package com.bilgeadam.service;

import com.bilgeadam.entity.Question;
import com.bilgeadam.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;
	
	public Question save(Question question) {
		return questionRepository.save(question);
	}
	
	public List<Question> findAllByIdIn(List<String> ids){
		return questionRepository.findAllByIdIn(ids);
	}
}
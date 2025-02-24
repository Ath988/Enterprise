package com.bilgeadam.service;

import com.bilgeadam.entity.Answer;
import com.bilgeadam.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
	private final AnswerRepository answerRepository;
	
	public Answer save(Answer answer) {
		return answerRepository.save(answer);
	}
	public List<Answer> findAllByIdIn(List<String> ids) {
		return answerRepository.findAllByIdIn(ids);
	}
	
	public List<Answer> findAllByIds(List<String> answerIds) {
		return answerRepository.findAllByIdIn(answerIds);
	}
}
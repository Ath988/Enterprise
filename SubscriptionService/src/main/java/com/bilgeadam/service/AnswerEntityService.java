package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddAnswerRequestDto;
import com.bilgeadam.dto.request.UpdateAnswerDto;
import com.bilgeadam.entity.AnswerEntity;
import com.bilgeadam.entity.Faq;
import com.bilgeadam.repository.AnswerEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerEntityService {
	private final AnswerEntityRepository answerEntityRepository;
	private final FaqService faqService;
	
	public List<AnswerEntity> getAllAnswers() {
		return answerEntityRepository.findAll();
	}
	
	public AnswerEntity updateAnswer(UpdateAnswerDto dto) {
		AnswerEntity existingAnswer = answerEntityRepository.findById(dto.id()).orElseThrow(()->new RuntimeException("cevap bulunamadı"));
		Optional<Faq> faq = faqService.findbyId(dto.faqId());
		if (faq.isEmpty()) {
			throw new RuntimeException("faq not found");
		}
		return answerEntityRepository.save(existingAnswer);
	}
	
	public Boolean createAnswer(AddAnswerRequestDto dto) {
		Optional<Faq> faq = faqService.findbyId(dto.faqId());
		if (faq.isEmpty()) {
			throw new RuntimeException("faq not found");
		}
		AnswerEntity answer = AnswerEntity.builder().answer(dto.answer()).faqId(dto.faqId()).build();
		
		answerEntityRepository.save(answer);
		return true;
	}
	
	public Boolean deleteAnswer(Long id) {
		answerEntityRepository.deleteById(id);
		return true;
	}
}
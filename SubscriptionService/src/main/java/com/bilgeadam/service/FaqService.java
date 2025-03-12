package com.bilgeadam.service;

import com.bilgeadam.dto.request.UpdateFaqDto;
import com.bilgeadam.entity.Faq;
import com.bilgeadam.repository.FaqRepository;
import com.bilgeadam.repository.PricingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FaqService {
	private final FaqRepository faqRepository;
	private final PricingPlanRepository pricingPlanRepository;
	
	public List<Faq> getAllFaqs() {
		return faqRepository.findAll();
	}
	
	public Boolean createFaq(String question) {
		Faq faq = Faq.builder().question(question).build();
		faqRepository.save(faq);
		return true;
	}
	
	public Faq updateFaq(UpdateFaqDto dto) {
		Faq existingFaq = faqRepository.findById(dto.id()).orElseThrow(()-> new RuntimeException("soru bulunamadı"));
		
		existingFaq.setQuestion(dto.question());
		
		return faqRepository.save(existingFaq);
	}
	
	public Boolean deleteFaq(Long faqId) {
		faqRepository.deleteById(faqId);
		return true;
	}
	
	public Optional<Faq> findbyId(Long faqId) {
		return faqRepository.findById(faqId);
	
	}
}
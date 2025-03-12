package com.bilgeadam.service;

import com.bilgeadam.entity.Option;
import com.bilgeadam.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {
	private final OptionRepository optionRepository;
	
	public Option save(Option option) {
		return optionRepository.save(option);
	}
	
	public List<Option> findAllByIdIn(List<String> ids) {
		return optionRepository.findAllByIdIn(ids);
	}
	
	public List<Option> findAllByQuestionId(String id) {
		return optionRepository.findAllByQuestionId(id);
	}
}
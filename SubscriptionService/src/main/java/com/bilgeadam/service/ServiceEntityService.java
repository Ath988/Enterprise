package com.bilgeadam.service;

import com.bilgeadam.dto.request.UpdateServiceDto;
import com.bilgeadam.entity.ServiceEntity;
import com.bilgeadam.repository.ServiceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceEntityService {
	private final ServiceEntityRepository serviceEntityRepository;
	
	
	public List<ServiceEntity> getAllServices() {
		return serviceEntityRepository.findAll();
	}
	
	public ServiceEntity updateServicePlan(UpdateServiceDto dto) {
		ServiceEntity existinService = serviceEntityRepository.findById(dto.id()).orElseThrow(()-> new RuntimeException(
				"Hizmet bulunamadı"));
		
		existinService.setTitle(dto.title());
		existinService.setDescriptions(dto.descriptions());
		
		return serviceEntityRepository.save(existinService);
	}
}
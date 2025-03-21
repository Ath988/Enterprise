package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddPerformerRequestDto;
import com.bilgeadam.dto.request.UpdatePerformerRequestDto;
import com.bilgeadam.entity.Performer;
import com.bilgeadam.entity.enums.Status;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.PerformerMapper;
import com.bilgeadam.repository.PerformerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformerService {
	private final PerformerRepository performerRepository;
	
	public void addPerformer(AddPerformerRequestDto dto) {
		// E-posta adresi zaten kayıtlı mı kontrol et
		if (performerRepository.findByEmail(dto.email()).isPresent()) {
			throw new CRMServiceException(ErrorType.EMAIL_ALREADY_EXISTS);
		}
		
		Performer performer = PerformerMapper.INSTANCE.fromAddPerformerRequestDto(dto);
		performerRepository.save(performer);
	}
	
	/** 📌 Tüm Performer'ları listeler */
	public List<Performer> getAllPerformers() {
		return performerRepository.findAll();
	}
	
	/** 📌 ID'ye göre Performer getirir */
	public Performer getPerformerById(Long id) {
		return performerRepository.findById(id)
		                          .orElseThrow(() -> new CRMServiceException(ErrorType.PERFORMER_NOT_FOUND));
	}
	
	/** 📌 E-posta adresine göre Performer getirir */
	public Performer getPerformerByEmail(String email) {
		return performerRepository.findByEmail(email)
		                          .orElseThrow(() -> new CRMServiceException(ErrorType.PERFORMER_NOT_FOUND));
	}
	
	public void updatePerformer(Long performerId, UpdatePerformerRequestDto dto) {
		// 1️⃣ Performer'ı ID ile bul
		Performer performer = performerRepository.findById(performerId)
		                                         .orElseThrow(() -> new CRMServiceException(ErrorType.PERFORMER_NOT_FOUND));
		
		// 2️⃣ Eğer Performer INACTIVE durumundaysa, önce ACTIVE yapılmalı!
		if (performer.getStatus() == Status.INACTIVE && dto.status() != Status.ACTIVE) {
			throw new CRMServiceException(ErrorType.TICKET_INACTIVE_CANNOT_UPDATE);
		}
		
		// 3️⃣ Eğer Status ACTIVE olarak güncellendiyse, artık diğer güncellemeler yapılabilir.
		if (dto.status() == Status.ACTIVE) {
			performer.setStatus(Status.ACTIVE);
		}
		
		// 4️⃣ Eğer email güncellenmek isteniyorsa ve başka biri kullanıyorsa hata fırlat
		if (dto.email() != null && !dto.email().equals(performer.getEmail()) &&
				performerRepository.findByEmail(dto.email()).isPresent()) {
			throw new CRMServiceException(ErrorType.EMAIL_ALREADY_EXISTS);
		}
		
		// 5️⃣ Eğer telefon numarası güncellenmek isteniyorsa ve başka biri kullanıyorsa hata fırlat
		if (dto.phoneNumber() != null && !dto.phoneNumber().equals(performer.getPhoneNumber()) &&
				performerRepository.findByPhoneNumber(dto.phoneNumber()).isPresent()) {
			throw new CRMServiceException(ErrorType.PHONE_ALREADY_EXISTS);
		}
		
		// 6️⃣ Mapper kullanarak güncellemeyi gerçekleştir
		PerformerMapper.INSTANCE.updatePerformerFromDto(dto, performer);
		
		// 5️⃣ Güncellenmiş Performer'ı kaydet
		performerRepository.save(performer);
	}
	
	public void deletePerformer(Long id) {
		Performer performer = performerRepository.findById(id)
		                                         .orElseThrow(() -> new CRMServiceException(ErrorType.PERFORMER_NOT_FOUND));
		
		performerRepository.delete(performer);
	}
	
	public void deletePerformers(List<Long> performerIds) {
		if (performerIds == null || performerIds.isEmpty()) {
			throw new CRMServiceException(ErrorType.PERFORMER_NOT_FOUND);
		}
		performerRepository.deleteAllById(performerIds);
	}
}
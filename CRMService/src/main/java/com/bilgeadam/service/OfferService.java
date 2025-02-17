package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddOfferRequestDto;
import com.bilgeadam.dto.request.UpdateOfferRequestDto;
import com.bilgeadam.entity.Offer;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.OfferMapper;
import com.bilgeadam.repository.OfferRepository;
import com.bilgeadam.views.VwOffer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {
	private final OfferRepository offerRepository;
	
	/**
	 * 📌 Yeni bir teklif (Offer) oluşturur ve veritabanına kaydeder.
	 */
	public void createOffer(AddOfferRequestDto dto) {
		Offer offer = OfferMapper.INSTANCE.toEntity(dto);
		offerRepository.save(offer);
	}
	
	public List<VwOffer> getAllOffers() {
		return offerRepository.findAllOffersWithCustomerInfo();
	}
	
	/**
	 * 📌 Belirli bir `id`'ye sahip teklifi getirir.
	 */
	public List<VwOffer> getOfferById(Long offerId) {
		offerRepository.findById(offerId)
		                             .orElseThrow(() -> new CRMServiceException(ErrorType.OFFER_NOT_FOUND));
		return offerRepository.findOfferWithCustomerInfoById(offerId);
	}
	
	/**
	 * 📌 Teklifi günceller.
	 */
	public void updateOffer(Long offerId, UpdateOfferRequestDto dto) {
		Offer offer = offerRepository.findById(offerId)
		                             .orElseThrow(() -> new CRMServiceException(ErrorType.OFFER_NOT_FOUND));
		
		offer.setStatus(dto.offerStatus());
		offer.setCustomerId(dto.customerId());
		OfferMapper.INSTANCE.updateOfferFromDto(dto, offer); // DTO verileri mevcut entity'ye aktarılıyor.
		
		offerRepository.save(offer);
	}
	
	/**
	 * 📌 Teklifi `id`'ye göre siler.
	 */
	public void deleteOffer(Long offerId) {
		Offer offer = offerRepository.findById(offerId)
		                             .orElseThrow(() -> new CRMServiceException(ErrorType.OFFER_NOT_FOUND));
		offerRepository.delete(offer);
	}
}
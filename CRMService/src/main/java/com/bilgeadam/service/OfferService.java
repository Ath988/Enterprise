package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddOfferRequestDto;
import com.bilgeadam.dto.request.UpdateOfferRequestDto;
import com.bilgeadam.entity.Customer;
import com.bilgeadam.entity.Offer;
import com.bilgeadam.entity.enums.OfferStatus;
import com.bilgeadam.entity.enums.Status;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.OfferMapper;
import com.bilgeadam.repository.CustomerRepository;
import com.bilgeadam.repository.OfferRepository;
import com.bilgeadam.views.VwOffer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {
	private final OfferRepository offerRepository;
	private final CustomerRepository customerRepository;
	private final MailService mailService;
	
	/**
	 * 📌 Yeni bir teklif (Offer) oluşturur ve veritabanına kaydeder.
	 */
	public void addOffer(AddOfferRequestDto dto) {
		
		Customer customer = customerRepository.findByProfileEmail(dto.customerEmail())
		                                      .orElseThrow(() -> new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND));
		
		Offer offer = OfferMapper.INSTANCE.toEntity(dto);
		offer.setCustomerId(customer.getCustomerId());
		offer.setCustomerName(customer.getProfile().getFirstName() + " " + customer.getProfile().getLastName());
		offer.setCustomerEmail(customer.getProfile().getEmail()); // 📌 Email set ediliyor
		
		offerRepository.save(offer);
		
		mailService.sendOfferEmail(offer.getId(), dto.customerEmail(), dto.title());
	}
	
	/** 📌 Teklifi kabul etme */
	public void acceptOffer(Long offerId) {
		Offer offer = offerRepository.findById(offerId)
		                             .orElseThrow(() -> new CRMServiceException(ErrorType.OFFER_NOT_FOUND));
		
		offer.setIsAccepted(true);
		offer.setOfferStatus(OfferStatus.ACCEPTED);
		offerRepository.save(offer);
		
		// Müşteri e-postasını bul ve bilgilendirme maili gönder
		String customerEmail = getCustomerEmailById(offer.getCustomerId());
		mailService.sendOfferAcceptedEmail(customerEmail, offer.getOfferDetail().getTitle());
	}
	
	/** 📌 Teklifi reddetme */
	public void rejectOffer(Long offerId) {
		Offer offer = offerRepository.findById(offerId)
		                             .orElseThrow(() -> new CRMServiceException(ErrorType.OFFER_NOT_FOUND));
		
		offer.setIsAccepted(false);
		offer.setOfferStatus(OfferStatus.DECLINED);
		offerRepository.save(offer);
		
		// Müşteri e-postasını bul ve bilgilendirme maili gönder
		String customerEmail = getCustomerEmailById(offer.getCustomerId());
		mailService.sendOfferRejectedEmail(customerEmail, offer.getOfferDetail().getTitle());
	}
	
	/** 📌 Müşteri ID ile e-posta adresini bulma */
	private String getCustomerEmailById(Long customerId) {
		return customerRepository.findById(customerId)
		                         .orElseThrow(() -> new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND))
		                         .getProfile()
		                         .getEmail();
	}
	
	public List<Offer> getAllOffers() {
		return offerRepository.findAll();
	}
	
	/**
	 * 📌 Belirli bir `id`'ye sahip teklifi getirir.
	 */
	public Offer getOfferById(Long offerId) {
		return offerRepository.findById(offerId)
		                      .orElseThrow(() -> new CRMServiceException(ErrorType.OFFER_NOT_FOUND));
	}
	
	/**
	 * 📌 Teklifi günceller.
	 */
	public void updateOffer(Long offerId, UpdateOfferRequestDto dto) {
		Offer offer = offerRepository.findById(offerId)
		                             .orElseThrow(() -> new CRMServiceException(ErrorType.OFFER_NOT_FOUND));
		if (offer.getStatus() == Status.INACTIVE && dto.status() != Status.ACTIVE){
			throw new CRMServiceException(ErrorType.OFFER_INACTIVE_CANNOT_UPDATE);
		}
		
		if (dto.status() == Status.ACTIVE) {
			offer.setStatus(Status.ACTIVE);
		}
		
		// 📌 Teklifin durumuna (offerStatus) göre geçiş kuralları belirleme
		if (!isValidStatusChange(offer.getOfferStatus(), dto.offerStatus())) {
			throw new CRMServiceException(ErrorType.INVALID_OFFER_STATUS_CHANGE);
		}
		
		OfferMapper.INSTANCE.updateOfferFromDto(dto, offer); // DTO verileri mevcut entity'ye aktarılıyor.
		
		offerRepository.save(offer);
	}
	
	/**
	 * 📌 OfferStatus için geçerli geçiş kurallarını belirleyen yardımcı metot
	 */
	private boolean isValidStatusChange(OfferStatus currentStatus, OfferStatus newStatus) {
		return switch (currentStatus) {
			case PENDING -> newStatus == OfferStatus.ACCEPTED || newStatus == OfferStatus.DECLINED || newStatus == OfferStatus.PENDING;
			case ACCEPTED -> newStatus == OfferStatus.EXPIRED || newStatus == OfferStatus.ACCEPTED; // Kabul edilen teklif sadece EXPIRED olabilir
			case DECLINED -> false; // Reddedilen teklif başka statüye çevrilemez
			case EXPIRED -> false; // Süresi dolmuş teklif güncellenemez
		};
	}
	
	/**
	 * 📌 Teklifi `id`'ye göre siler.
	 */
	public void deleteOffer(Long offerId) {
		Offer offer = offerRepository.findById(offerId)
		                             .orElseThrow(() -> new CRMServiceException(ErrorType.OFFER_NOT_FOUND));
		offerRepository.delete(offer);
	}
	
	public void deleteOffers(List<Long> offerIds) {
		if (offerIds == null || offerIds.isEmpty()) {
			throw new CRMServiceException(ErrorType.OFFER_NOT_FOUND);
		}
		offerRepository.deleteAllById(offerIds);
	}
}
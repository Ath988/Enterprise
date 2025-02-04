package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddSupportTicketRequestDto;
import com.bilgeadam.dto.request.UpdateSupportTicketRequestDto;
import com.bilgeadam.entity.SupportTicket;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.SupportTicketMapper;
import com.bilgeadam.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketService {
	private final SupportTicketRepository supportTicketRepository;
	
	public void addSupportTicket(AddSupportTicketRequestDto dto) {
		SupportTicket supportTicket = SupportTicketMapper.INSTANCE.fromAddSupportTicketDto(dto);
		supportTicketRepository.save(supportTicket);
	}
	
	public List<SupportTicket> getAllSupportTickets() {
		return supportTicketRepository.findAll();
	}
	
	public SupportTicket getSupportTicketById(Long id) {
		return supportTicketRepository.findById(id)
		                              .orElseThrow(() -> new CRMServiceException(ErrorType.SUPPORT_TICKET_NOT_FOUND));
	}
	
	public void updateSupportTicket(Long id, UpdateSupportTicketRequestDto dto) {
		SupportTicket supportTicket = supportTicketRepository.findById(id)
		                                                     .orElseThrow(() -> new CRMServiceException(ErrorType.SUPPORT_TICKET_NOT_FOUND));
		SupportTicketMapper.INSTANCE.updateSupportTicketFromDto(dto, supportTicket);
		supportTicketRepository.save(supportTicket);
	}
	
	public void deleteSupportTicket(Long id) {
		SupportTicket supportTicket = supportTicketRepository.findById(id)
		                                                     .orElseThrow(() -> new CRMServiceException(ErrorType.SUPPORT_TICKET_NOT_FOUND));
		supportTicketRepository.delete(supportTicket);
	}
	
	public List<SupportTicket> getSupportTicketsByCustomerId(Long customerId) {
		return supportTicketRepository.findByCustomerId(customerId);
	}
	
	public List<SupportTicket> getSupportTicketsByStatus(String status) {
		return supportTicketRepository.findByStatus(status);
	}
	
	public List<SupportTicket> getSupportTicketsByCustomerAndSubject(Long customerId, String subject) {
		return supportTicketRepository.findByCustomerIdAndSubject(customerId, subject);
	}
	
	public List<SupportTicket> getLatestSupportTickets() {
		return supportTicketRepository.findTop10ByOrderByCreatedAtDesc();
	}
}
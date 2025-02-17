package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddTicketRequestDto;
import com.bilgeadam.dto.request.UpdateTicketRequestDto;
import com.bilgeadam.entity.ActivityPerformer;
import com.bilgeadam.entity.Ticket;
import com.bilgeadam.entity.TicketActivity;
import com.bilgeadam.entity.enums.ActivityType;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.ActivityPerformerMapper;
import com.bilgeadam.mapper.TicketMapper;
import com.bilgeadam.repository.TicketActivityRepository;
import com.bilgeadam.repository.TicketRepository;
import com.bilgeadam.views.TicketDetailView;
import com.bilgeadam.views.VwTicket;
import com.bilgeadam.views.VwTicketActivity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
	private final TicketRepository ticketRepository;
	private final TicketActivityRepository ticketActivityRepository;
	
	
	public void addTicket(AddTicketRequestDto dto){
		try {
			// 1️⃣ Ticket oluştur
			Ticket ticket = TicketMapper.INSTANCE.toTicket(dto);
			Ticket save = ticketRepository.save(ticket);
			
			// 2️⃣ TicketActivity oluştur
			TicketActivity ticketActivity = TicketMapper.INSTANCE.toTicketActivity(dto);
			ticketActivity.setTicketId(save.getId());
			ticketActivity.setPerformedBy(ActivityPerformerMapper.INSTANCE.toActivityPerformer(dto));
			ticketActivityRepository.save(ticketActivity);
		} catch (Exception e) {
			throw new CRMServiceException(ErrorType.TICKET_CREATION_FAILED);
		}
	}
	
	public List<VwTicket> getAllTickets() {
		List<VwTicket> tickets = ticketRepository.findAllTicketsWithCustomerInfo();
		if (tickets.isEmpty()) {
			throw new CRMServiceException(ErrorType.TICKET_NOT_FOUND);
		}
		return tickets;
	}
	
	/**
	 * 📌 Belirli Bir Ticket'i ID'ye Göre Getir
	 */
	public TicketDetailView getTicketById(Long ticketId) {
		// Ticket kontrolü
		VwTicket ticket = ticketRepository.findTicketWithCustomerInfoById(ticketId);
		if (ticket == null) {
			throw new CRMServiceException(ErrorType.TICKET_NOT_FOUND);
		}
		
		// Ticket Activity'lerini getir
		List<VwTicketActivity> activities = ticketActivityRepository.findActivitiesByTicketId(ticketId);
		
		return new TicketDetailView(ticket, activities);
	}
	
	/**
	 * 📌 Ticket Güncelle (`void` dönüşlü)
	 */
	public void updateTicket(Long ticketId, UpdateTicketRequestDto dto) {
		try {
			// 1️⃣ Ticket var mı kontrol et
			Ticket ticket = ticketRepository.findById(ticketId)
			                                .orElseThrow(() -> new CRMServiceException(ErrorType.TICKET_NOT_FOUND));
			
			// 2️⃣ Ticket alanlarını güncelle
			TicketMapper.INSTANCE.updateTicketFromDto(dto, ticket);
			ticketRepository.save(ticket);
			
			// 3️⃣ Güncelleme Activity oluştur
			TicketActivity activity = new TicketActivity();
			activity.setTicketId(ticket.getId());
			activity.setType(dto.type());
			activity.setTimestamp(LocalDateTime.now());
			activity.setContent(dto.content());
			
			// 4️⃣ Var olan Performer bilgilerini güncelle
			ActivityPerformer performer = new ActivityPerformer();
			ActivityPerformerMapper.INSTANCE.updateActivityPerformerFromDto(dto, performer);
			activity.setPerformedBy(performer);
			
			ticketActivityRepository.save(activity);
		}catch (Exception e) {
			throw new CRMServiceException(ErrorType.TICKET_UPDATE_FAILED);
		}
	}
	
	public void deleteTicket(Long ticketId) {
		Ticket ticket = ticketRepository.findById(ticketId)
		                                .orElseThrow(() -> new CRMServiceException(ErrorType.TICKET_NOT_FOUND));
		ticketRepository.delete(ticket);
		ticketActivityRepository.deleteAll(ticketActivityRepository.findAllByTicketId(ticketId));
	}
}
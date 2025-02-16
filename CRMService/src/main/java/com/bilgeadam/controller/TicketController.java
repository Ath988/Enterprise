package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddTicketRequestDto;
import com.bilgeadam.dto.request.UpdateTicketRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.service.TicketService;
import com.bilgeadam.views.TicketDetailView;
import com.bilgeadam.views.VwTicket;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/dev/ticket")
@RequiredArgsConstructor
public class TicketController {
	private final TicketService ticketService;
	
	@PostMapping("/add-ticket")
	public ResponseEntity<BaseResponse<Boolean>> addTicket(@RequestBody @Valid AddTicketRequestDto dto){
		ticketService.addTicket(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Yeni bilet eklendi.")
		                                     .build()
		);
	}
	
	@GetMapping("/all-tickets")
	public ResponseEntity<BaseResponse<List<VwTicket>>> getAllTickets() {
		List<VwTicket> tickets = ticketService.getAllTickets();
		return ResponseEntity.ok(BaseResponse.<List<VwTicket>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(tickets)
		                                     .message("Tüm biletler başarıyla listelendi.")
		                                     .build());
	}
	
	@GetMapping("/get-ticket-by-id/{ticketId}")
	public ResponseEntity<BaseResponse<TicketDetailView>> getTicketById(@PathVariable Long ticketId) {
		TicketDetailView ticketDetail = ticketService.getTicketById(ticketId);
		return ResponseEntity.ok(BaseResponse.<TicketDetailView>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(ticketDetail)
		                                     .message("Bilet detayları başarıyla getirildi.")
		                                     .build());
	}
	
	@PutMapping("/update-ticket/{ticketId}")
	public ResponseEntity<BaseResponse<Boolean>> updateTicket(
			@PathVariable Long ticketId,
			@RequestBody @Valid UpdateTicketRequestDto dto) {
		
		ticketService.updateTicket(ticketId, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .message("Ticket başarıyla güncellendi ve aktivite kaydedildi.")
		                                     .build());
	}
	
	@DeleteMapping("/delete-ticket/{ticketId}")
	public ResponseEntity<BaseResponse<Boolean>> deleteTicket(@PathVariable Long ticketId) {
		ticketService.deleteTicket(ticketId);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Ticket başarıyla silindi.")
		                                     .build()
		);
	}
}
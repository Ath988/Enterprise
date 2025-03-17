package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddTicketRequestDto;
import com.bilgeadam.dto.request.UpdateTicketRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Ticket;
import com.bilgeadam.service.TicketService;
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
	
	@PostMapping("/add")
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
	
	/** 📌 ID'ye göre Ticket getirir */
	@GetMapping("id" + "/{ticketId}")
	public ResponseEntity<BaseResponse<Ticket>> getTicketById(@PathVariable Long ticketId) {
		Ticket ticket = ticketService.getTicketById(ticketId);
		return ResponseEntity.ok(BaseResponse.<Ticket>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(ticket)
		                                     .message("Ticket bilgisi getirildi.")
		                                     .build()
		);
	}
	
	/** 📌 Tüm Ticket'ları getirir */
	@GetMapping("all")
	public ResponseEntity<BaseResponse<List<Ticket>>> getAllTickets() {
		List<Ticket> tickets = ticketService.getAllTickets();
		return ResponseEntity.ok(BaseResponse.<List<Ticket>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(tickets)
		                                     .message("Tüm ticket'lar listelendi.")
		                                     .build()
		);
	}
	
	@PutMapping("update" + "/{ticketId}")
	public ResponseEntity<BaseResponse<Boolean>> updateTicket(@PathVariable Long ticketId, @RequestBody @Valid UpdateTicketRequestDto dto) {
		ticketService.updateTicket(ticketId, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Ticket başarıyla güncellendi.")
		                                     .build()
		);
	}
	
	/** 📌 Ticket silme işlemi */
	@DeleteMapping("delete" + "/{ticketId}")
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
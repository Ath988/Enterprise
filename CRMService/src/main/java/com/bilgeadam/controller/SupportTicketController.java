package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddSupportTicketRequestDto;
import com.bilgeadam.dto.request.UpdateSupportTicketRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.SupportTicket;
import com.bilgeadam.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequestMapping(SUPPORT_TICKET)
@RequiredArgsConstructor
public class SupportTicketController {
	private final SupportTicketService supportTicketService;
	
	@PostMapping(ADD_SUPPORT_TICKET)
	public ResponseEntity<BaseResponse<Boolean>> addSupportTicket(@RequestBody @Valid AddSupportTicketRequestDto dto) {
		supportTicketService.addSupportTicket(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Yeni destek bileti eklendi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_ALL_SUPPORT_TICKETS)
	public ResponseEntity<BaseResponse<List<SupportTicket>>> getAllSupportTickets() {
		return ResponseEntity.ok(BaseResponse.<List<SupportTicket>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(supportTicketService.getAllSupportTickets())
		                                     .message("Tüm destek biletleri getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SUPPORT_TICKET_BY_ID)
	public ResponseEntity<BaseResponse<SupportTicket>> getSupportTicketById(@RequestParam Long id) {
		return ResponseEntity.ok(BaseResponse.<SupportTicket>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(supportTicketService.getSupportTicketById(id))
		                                     .message("Destek bileti getirildi.")
		                                     .build()
		);
	}
	
	@PutMapping(UPDATE_SUPPORT_TICKET)
	public ResponseEntity<BaseResponse<Boolean>> updateSupportTicket(@RequestParam Long id, @RequestBody @Valid UpdateSupportTicketRequestDto dto) {
		supportTicketService.updateSupportTicket(id, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Destek bileti güncellendi.")
		                                     .build()
		);
	}
	
	@DeleteMapping(DELETE_SUPPORT_TICKET)
	public ResponseEntity<BaseResponse<Boolean>> deleteSupportTicket(@RequestParam Long id) {
		supportTicketService.deleteSupportTicket(id);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Destek bileti silindi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SUPPORT_TICKETS_BY_CUSTOMER_ID)
	public ResponseEntity<BaseResponse<List<SupportTicket>>> getSupportTicketsByCustomerId(@RequestParam Long customerId) {
		return ResponseEntity.ok(BaseResponse.<List<SupportTicket>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(supportTicketService.getSupportTicketsByCustomerId(customerId))
		                                     .message("Müşteri ID'ye göre destek biletleri getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SUPPORT_TICKETS_BY_STATUS)
	public ResponseEntity<BaseResponse<List<SupportTicket>>> getSupportTicketsByStatus(@RequestParam String status) {
		return ResponseEntity.ok(BaseResponse.<List<SupportTicket>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(supportTicketService.getSupportTicketsByStatus(status))
		                                     .message("Statüye göre destek biletleri getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SUPPORT_TICKETS_BY_CUSTOMER_AND_SUBJECT)
	public ResponseEntity<BaseResponse<List<SupportTicket>>> getSupportTicketsByCustomerAndSubject(@RequestParam Long customerId, @RequestParam String subject) {
		return ResponseEntity.ok(BaseResponse.<List<SupportTicket>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(supportTicketService.getSupportTicketsByCustomerAndSubject(customerId, subject))
		                                     .message("Müşteri ID ve konuya göre destek biletleri getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_LATEST_SUPPORT_TICKETS)
	public ResponseEntity<BaseResponse<List<SupportTicket>>> getLatestSupportTickets() {
		return ResponseEntity.ok(BaseResponse.<List<SupportTicket>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(supportTicketService.getLatestSupportTickets())
		                                     .message("Son 10 destek bileti getirildi.")
		                                     .build()
		);
	}
}
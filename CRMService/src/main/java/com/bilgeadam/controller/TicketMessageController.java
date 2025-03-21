package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddTicketMessageRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.TicketMessage;
import com.bilgeadam.service.TicketMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/dev/ticketmessage")
@RequiredArgsConstructor
public class TicketMessageController {
	private final TicketMessageService ticketMessageService;
	
	/** 📌 Müşterinin destek mesajını gönderme */
	@PostMapping("/submit")
	public ResponseEntity<BaseResponse<Boolean>> submitTicketMessage(@RequestBody @Valid AddTicketMessageRequestDto dto) {
		ticketMessageService.submitTicketMessage(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Müşteri mesajı alındı ve kayıt edildi.")
		                                     .build()
		);
	}
	
	/** 📌 Tüm destek mesajlarını listeleme */
	@GetMapping("/list")
	public ResponseEntity<BaseResponse<List<TicketMessage>>> getAllTicketMessages() {
		List<TicketMessage> messages = ticketMessageService.getAllMessages();
		return ResponseEntity.ok(BaseResponse.<List<TicketMessage>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(messages)
		                                     .message("Destek mesajları başarıyla getirildi.")
		                                     .build());
	}
	
}
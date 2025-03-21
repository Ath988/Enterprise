package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddTicketMessageRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.service.TicketMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddFeedbackRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Feedback;
import com.bilgeadam.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
	
	private final FeedbackService feedbackService;
	
	@PostMapping("/submit/{ticketId}")
	public ResponseEntity<BaseResponse<Feedback>> submitFeedback(
			@PathVariable Long ticketId,
			@RequestBody @Valid AddFeedbackRequestDto dto) {
		Feedback feedback = feedbackService.submitFeedback(ticketId, dto);
		return ResponseEntity.ok(BaseResponse.<Feedback>builder()
				                         .code(200)
				                         .success(true)
				                         .data(feedback)
				                         .message("Geri bildirim başarıyla kaydedildi.")
				                         .build());
	}
	
	/** 📌 Ticket ID'ye Göre Geri Bildirimleri Listeleme */
	@GetMapping("/id" + "/{ticketId}")
	public ResponseEntity<BaseResponse<List<Feedback>>> getFeedbackByTicketId(@PathVariable Long ticketId) {
		List<Feedback> feedbackList = feedbackService.getFeedbackByTicketId(ticketId);
		return ResponseEntity.ok(BaseResponse.<List<Feedback>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(feedbackList)
		                                     .message("Ticket ID'ye ait geri bildirimler listelendi.")
		                                     .build());
	}
	
	/** 📌 Kullanıcının E-posta Adresine Göre Geri Bildirimleri Listeleme */
	@GetMapping("/email")
	public ResponseEntity<BaseResponse<List<Feedback>>> getFeedbackByEmail(@RequestParam String email) {
		List<Feedback> feedbackList = feedbackService.getFeedbackByEmail(email);
		return ResponseEntity.ok(BaseResponse.<List<Feedback>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(feedbackList)
		                                     .message("Belirtilen e-posta adresine ait geri bildirimler listelendi.")
		                                     .build());
	}
}
package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AnnouncementRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Announcement;
import com.bilgeadam.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(ANNOUNCEMENT)
@CrossOrigin("*")
public class AnnouncementController {
	
	private final AnnouncementService announcementService;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@PostMapping(CREATE_ANNOUNCEMENT)
	public ResponseEntity<BaseResponse<Boolean>> createAnnouncement(@RequestHeader(value = "Authorization", required =
			false) String token, @RequestBody  AnnouncementRequestDto dto) {
		return ResponseEntity.ok(BaseResponse.<Boolean>builder().code(200)
		                                     .data(announcementService.createAnnouncement(token,dto)).success(true)
		                                     .message("Duyuru başarı ile oluşturuldu.").build());
	}
	
	
	@GetMapping(GETALLANNOUNCEMENT)
	public ResponseEntity<BaseResponse<List<Announcement>>> getAnnouncements(@RequestHeader(value = "Authorization",
			required =
					false) String token) {
		
		List<Announcement> announcements = announcementService.getAnnouncements(token);
		return ResponseEntity.ok(BaseResponse.<List<Announcement>>builder().code(200)
				                         .data(announcements)
				                         .success(true)
				                         .message("Duyurular başarıyla listelendi.")
				                         .build());
	}
}
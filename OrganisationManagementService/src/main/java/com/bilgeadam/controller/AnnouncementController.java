package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AnnouncementRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Announcement;
import com.bilgeadam.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constants.RestApis.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(ANNOUNCEMENT)
@CrossOrigin("*")
public class AnnouncementController {
	
	private final AnnouncementService announcementService;
	
	@PostMapping(CREATE_ANNOUNCEMENT)
	public ResponseEntity<BaseResponse<Boolean>> createAnnouncement(@RequestHeader(value = "Authorization", required =
			false) String token, @RequestBody  AnnouncementRequestDto dto) {
		return ResponseEntity.ok(BaseResponse.<Boolean>builder().code(200)
		                                     .data(announcementService.createAnnouncement(token,dto)).success(true)
		                                     .message("Duyuru başarı ile oluşturuldu.").build());
	}
}
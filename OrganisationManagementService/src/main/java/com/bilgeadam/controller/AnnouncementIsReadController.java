package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AnnouncementRequestDto;
import com.bilgeadam.dto.request.UpdateAssetRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.service.AnnouncementIsReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constants.RestApis.ANNOUNCEMENT;
import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ANNOUNCEMENT_IS_READ)
@CrossOrigin("*")
public class AnnouncementIsReadController {

    private final AnnouncementIsReadService announcementIsReadService;


    @PutMapping("/update-announcement-is-read")
    public ResponseEntity<BaseResponse<Boolean>> updateAnnouncementIsRead(
            @RequestHeader(value = "Authorization", required = false) String token, Long announcementId){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Okundu bilgisi güncelleme işlemi başarılı.")
                .data(announcementIsReadService.announcementIsReadTrue(token,announcementId))
                .build());
    }
}


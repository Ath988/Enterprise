package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AnnouncementRequestDto;
import com.bilgeadam.dto.request.UpdateAssetRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.EmployeeReadAnnouncementResponseDto;
import com.bilgeadam.entity.Announcement;
import com.bilgeadam.service.AnnouncementIsReadService;
import feign.Param;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @GetMapping(GETALLREADERSANNOUNCEMENT)
//    public ResponseEntity<BaseResponse<List<EmployeeReadAnnouncementResponseDto>>> getEmployeeReadersAnnouncement(@RequestParam Long announcementId) {
//        return ResponseEntity.ok(BaseResponse.<List<EmployeeReadAnnouncementResponseDto>>builder()
//                .code(200)
//                .data(announcementIsReadService.getAllReaders(announcementId)) // Duyuru ID'sini alıyoruz
//                .success(true)
//                .message("Duyuruyu görüntüleme işlemi başarıyla gerçekleştirildi.")
//                .build());
//    }


}


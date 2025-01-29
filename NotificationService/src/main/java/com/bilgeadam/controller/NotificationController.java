package com.bilgeadam.controller;

import com.bilgeadam.constant.RestApis;
import com.bilgeadam.dto.request.NotificationMessageRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Notification;
import com.bilgeadam.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(RestApis.USER + "/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public ResponseEntity<BaseResponse<Notification>> sendNotification(@RequestBody NotificationMessageRequestDto message) {
        Notification notification = notificationService.createNotification(message);
        messagingTemplate.convertAndSend("/topic/notifications", notification);

        return ResponseEntity.ok(BaseResponse.<Notification>builder()
                .code(200)
                .data(notification)
                .message("Bildirim başarıyla gönderildi!")
                .success(true)
                .build());
    }

//    @GetMapping
//    public ResponseEntity<BaseResponse<List<Notification>>> getNotifications(@RequestHeader(value = "Authorization", required = false) String token) {
//        System.out.println("Authorization Header: " + token);
//        if (token == null || !token.startsWith("Bearer ")) {
//            throw new RuntimeException("Authorization header eksik veya geçersiz!");
//        }
//        String userToken = token.replace("Bearer ", "");
//        List<Notification> notifications = notificationService.getNotificationsByUserToken(userToken);
//
//        return ResponseEntity.ok(BaseResponse.<List<Notification>>builder()
//                .code(200)
//                .data(notifications)
//                .message("Bildirimler başarıyla getirildi!")
//                .success(true)
//                .build());
//    }



    @GetMapping
    public ResponseEntity<BaseResponse<List<Notification>>> getNotifications() {



        return ResponseEntity.ok(BaseResponse.<List<Notification>>builder()
                .code(200)
                .data(notificationService.getNotificationsByUser())
                .message("Bildirimler başarıyla getirildi!")
                .success(true)
                .build());
    }



    //Kişiye göre listeleme
    //Token eklicem, o tokena göre string validate yap

}

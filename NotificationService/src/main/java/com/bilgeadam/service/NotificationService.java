package com.bilgeadam.service;


import com.bilgeadam.dto.request.NotificationMessageRequestDto;
import com.bilgeadam.entity.Notification;

import com.bilgeadam.repository.NotificationRepository;
import com.bilgeadam.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JwtUtil jwtUtil;
    private final NotificationRepository notificationRepository;

    public Notification createNotification(NotificationMessageRequestDto message) {
        Notification notification = new Notification();
        notification.setTitle(message.title());
        notification.setDescription(message.description());
        notification.setDate(LocalDateTime.now());
        notification.setIsRead(message.isRead());
        return notificationRepository.save(notification);
    }


    public List<Notification> getNotificationsByUserToken(String token) {
        Long userId = jwtUtil.validateToken(token)
                .orElseThrow(() -> new RuntimeException("Geçersiz veya süresi dolmuş token!"));

        return notificationRepository.findByUserId(userId);
    }



    public List<Notification> getNotificationsByUser() {


        return notificationRepository.findAll();
    }
}

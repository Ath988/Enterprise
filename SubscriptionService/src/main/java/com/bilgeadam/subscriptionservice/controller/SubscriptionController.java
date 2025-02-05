package com.bilgeadam.subscriptionservice.controller;

import com.bilgeadam.subscriptionservice.entity.Subscription;
import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionType;
import com.bilgeadam.subscriptionservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.bilgeadam.subscriptionservice.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(SUBSCRIPTION)
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<Subscription> addSubscription(String userId, SubscriptionType subscriptionType) {
        return ResponseEntity.ok(subscriptionService.addSubscription(userId, subscriptionType));
    }

    @GetMapping
    public ResponseEntity<Subscription> getCurrentSubscription(String userId) {
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId)); // servis ve repo metodları eksik (endDate'i bugünden sonra olan subscription listesi getir)
    }
}

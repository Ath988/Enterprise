package com.bilgeadam.subscriptionservice.controller;

import com.bilgeadam.subscriptionservice.entity.Subscription;
import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionPlan;
import com.bilgeadam.subscriptionservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.bilgeadam.subscriptionservice.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(SUBSCRIPTION)
public class SubscriptionController { // TODO request'leri dto.request paketinde belirleyip yönetme  // TODO response'ları dto.response paketinde belirleyip yönetme
    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<Subscription> addSubscription(String userId, SubscriptionPlan subscriptionPlan) {
        return ResponseEntity.ok(subscriptionService.addSubscription(userId, subscriptionPlan));
    }

    @GetMapping
    public ResponseEntity<Subscription> getCurrentSubscription(String userId) {
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getSubscriptionHistory(String userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionHistory(userId));
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> changeSubscriptionPlan(String userId, SubscriptionPlan subscriptionPlan) {
        return ResponseEntity.ok(subscriptionService.updateSubscriptionPlan(userId, subscriptionPlan));
    }
}

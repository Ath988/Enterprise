package com.bilgeadam.subscriptionservice.controller;

import com.bilgeadam.subscriptionservice.dto.request.AddSubscriptionRequest;
import com.bilgeadam.subscriptionservice.dto.request.ChangeSubscriptionPlanRequest;
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
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<Subscription> addSubscription(AddSubscriptionRequest dto) {
        return ResponseEntity.ok(subscriptionService.addSubscription(dto));
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
    public ResponseEntity<Subscription> changeSubscriptionPlan(ChangeSubscriptionPlanRequest dto) {
        return ResponseEntity.ok(subscriptionService.updateSubscriptionPlan(dto));
    }
}

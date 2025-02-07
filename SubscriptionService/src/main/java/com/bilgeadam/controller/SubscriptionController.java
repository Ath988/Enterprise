package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddSubscriptionRequest;
import com.bilgeadam.dto.request.ChangeSubscriptionPlanRequest;
import com.bilgeadam.entity.Subscription;
import com.bilgeadam.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(SUBSCRIPTION)
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping(ADD_SUBSCRIPTION)
    public ResponseEntity<Subscription> addSubscription(AddSubscriptionRequest dto) {
        return ResponseEntity.ok(subscriptionService.addSubscription(dto));
    }

    @GetMapping(GET_CURRENT_SUBSCRIPTION)
    public ResponseEntity<Subscription> getCurrentSubscription(String userId) {
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @GetMapping(GET_SUBSCRIPTION_HISTORY)
    public ResponseEntity<List<Subscription>> getSubscriptionHistory(String userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionHistory(userId));
    }

    @GetMapping(UPDATE_SUBSCRIPTION)
    public ResponseEntity<Subscription> updateSubscriptionPlan(ChangeSubscriptionPlanRequest dto) {
        return ResponseEntity.ok(subscriptionService.updateSubscriptionPlan(dto));
    }
}

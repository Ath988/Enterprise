package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddSubscriptionRequest;
import com.bilgeadam.dto.request.ChangeSubscriptionPlanRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Subscription;
import com.bilgeadam.entity.enums.SubscriptionPlan;
import com.bilgeadam.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(SUBSCRIPTION)
@CrossOrigin("*")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping(ADD_SUBSCRIPTION)
    public ResponseEntity<BaseResponse<Subscription>> addSubscription(AddSubscriptionRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Subscription>builder()
                .data(subscriptionService.addSubscription(dto))
                .success(true)
                .code(200)
                .message("subscription added to database")
                .build());
    }

    @GetMapping(GET_CURRENT_SUBSCRIPTION)
    public ResponseEntity<BaseResponse<Subscription>> getCurrentSubscription(String userId) {
        return ResponseEntity.ok(BaseResponse.<Subscription>builder()
                .data(subscriptionService.getCurrentSubscription(userId))
                .success(true)
                .code(200)
                .message("current subscription fetched")
                .build());
    }

    @GetMapping(GET_SUBSCRIPTION_HISTORY)
    public ResponseEntity<BaseResponse<List<Subscription>>> getSubscriptionHistory(String userId) {
        return ResponseEntity.ok(BaseResponse.<List<Subscription>>builder()
                .data(subscriptionService.getSubscriptionHistory(userId))
                .success(true)
                .code(200)
                .message("subscription history fetched")
                .build());
    }

    @PostMapping(UPDATE_SUBSCRIPTION)
    public ResponseEntity<BaseResponse<Subscription>> updateSubscriptionPlan(@RequestBody @Valid ChangeSubscriptionPlanRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Subscription>builder()
                .data(subscriptionService.updateSubscriptionPlan(dto))
                .success(true)
                .code(200)
                .message("subscription updated")
                .build());
    }

    @GetMapping(CANCEL_SUBSCRIPTION)
    public ResponseEntity<BaseResponse<Subscription>> cancelCurrentSubscription(String userId) {
        return ResponseEntity.ok(BaseResponse.<Subscription>builder()
                .data(subscriptionService.cancelSubscription(userId))
                .success(true)
                .code(200)
                .message("current subscription fetched")
                .build());
    }

    @GetMapping(GET_ACTIVE_SUBSCRIPTION)
    public ResponseEntity<BaseResponse<SubscriptionPlan>> getActiveSubscriptionPlan(String userId) {
        return ResponseEntity.ok(BaseResponse.<SubscriptionPlan>builder()
                .data(subscriptionService.getCurrentActiveSubscriptionPlan(userId))
                .success(true)
                .code(200)
                .message("active subscription fetched")
                .build());
    }
}

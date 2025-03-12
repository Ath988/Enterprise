package com.bilgeadam.dto.request.otherServices;


import com.bilgeadam.entity.enums.SubscriptionPlan;

public record AddSubscriptionRequest(
        String token,
        Long userId,
        SubscriptionPlan subscriptionPlan
) {
}

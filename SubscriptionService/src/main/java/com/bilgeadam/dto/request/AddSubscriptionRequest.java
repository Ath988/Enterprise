package com.bilgeadam.dto.request;


import com.bilgeadam.entity.enums.SubscriptionPlan;

public record AddSubscriptionRequest(
        String token,
        SubscriptionPlan subscriptionPlan
) {
}

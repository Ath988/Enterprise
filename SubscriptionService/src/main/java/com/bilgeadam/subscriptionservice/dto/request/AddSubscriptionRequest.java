package com.bilgeadam.subscriptionservice.dto.request;

import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionPlan;

public record AddSubscriptionRequest(
        String userId,
        SubscriptionPlan subscriptionPlan
) {
}

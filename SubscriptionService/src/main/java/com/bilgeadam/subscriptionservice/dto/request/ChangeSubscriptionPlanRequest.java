package com.bilgeadam.subscriptionservice.dto.request;

import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionPlan;

public record ChangeSubscriptionPlanRequest(
        String userId,
        SubscriptionPlan subscriptionPlan
) {
}

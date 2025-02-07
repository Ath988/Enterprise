package com.bilgeadam.dto.request;


import com.bilgeadam.entity.enums.SubscriptionPlan;

public record ChangeSubscriptionPlanRequest(
        String userId,
        SubscriptionPlan subscriptionPlan
) {
}

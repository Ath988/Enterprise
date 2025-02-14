package com.bilgeadam.dto.request;


import com.bilgeadam.entity.enums.SubscriptionPlan;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record ChangeSubscriptionPlanRequest(
        String userId,
        @JsonSerialize
        SubscriptionPlan subscriptionPlan
) {
}

package com.bilgeadam.subscriptionservice.mapper;

import com.bilgeadam.subscriptionservice.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SubscriptionMapper {
    SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);

    Subscription subscriptionToSubscription(Subscription subscription);
}

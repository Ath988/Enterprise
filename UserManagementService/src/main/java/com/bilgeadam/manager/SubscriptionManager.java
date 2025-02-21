package com.bilgeadam.manager;

import com.bilgeadam.dto.request.otherServices.AddSubscriptionRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.enums.SubscriptionPlan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://localhost:8089/v1/dev/subscription", name = "subscriptionManager")
public interface SubscriptionManager {

    @PostMapping("/add")
    ResponseEntity<BaseResponse<Boolean>> addSubscription(@RequestBody AddSubscriptionRequest dto);

    @GetMapping("/get-user-sub")
    ResponseEntity<BaseResponse<SubscriptionPlan>> getUserSubscriptionPlan(@RequestParam Long userId);


}

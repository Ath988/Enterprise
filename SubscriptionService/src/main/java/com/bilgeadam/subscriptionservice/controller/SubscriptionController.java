package com.bilgeadam.subscriptionservice.controller;

import com.bilgeadam.subscriptionservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.bilgeadam.subscriptionservice.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(SUBSCRIPTION)
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
}

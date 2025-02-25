package com.inventoryservice.manager;


import com.inventoryservice.dto.request.NotificationMessageRequestDto;
import com.inventoryservice.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.inventoryservice.constants.Endpoints.NOTIFICATIONSENDER;

@FeignClient(url = "http://localhost:8090/v1/dev/notification", name = "notificationSender")
public interface NotificationManager {
	
	@PostMapping(NOTIFICATIONSENDER)
	ResponseEntity<BaseResponse<Boolean>> notificationSender(@RequestBody NotificationMessageRequestDto dto);
}
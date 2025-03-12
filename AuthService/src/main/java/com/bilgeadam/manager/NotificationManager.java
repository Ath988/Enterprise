package com.bilgeadam.manager;

import com.bilgeadam.dto.request.NotificationMessageRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.bilgeadam.constant.RestApis.NOTIFICATIONSENDER;

@FeignClient(url = "http://localhost:8090/v1/dev/notification", name = "notificationSender")
public interface NotificationManager {
	
	@PostMapping(NOTIFICATIONSENDER)
	ResponseEntity<BaseResponse<Boolean>> notificationSender(@RequestBody NotificationMessageRequestDto dto);
}
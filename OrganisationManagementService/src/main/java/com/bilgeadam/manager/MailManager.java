package com.bilgeadam.manager;

import com.bilgeadam.dto.request.EmailDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static com.bilgeadam.constants.RestApis.SEND_MAIL;

@FeignClient(url = "http://localhost:9093/v1/dev/mail", name = "mailManager")
public interface MailManager {

    @PostMapping(SEND_MAIL)
    ResponseEntity<Boolean> sendEmail(@RequestBody @Valid EmailDto emailDto);
}

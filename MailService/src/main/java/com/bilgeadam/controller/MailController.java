package com.bilgeadam.controller;

import com.bilgeadam.dto.request.EmailDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Mail;
import com.bilgeadam.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequestMapping(MAIL)
@RequiredArgsConstructor
@CrossOrigin("*")
public class MailController {
    private final MailService mailService;

    @PostMapping(SEND_MAIL)
    public ResponseEntity<Mail> sendEmail(@RequestBody @Valid EmailDto emailDto) {
        Mail email = mailService.createEmail(emailDto);
        mailService.sendEmail(email);

        return new ResponseEntity<>(email, HttpStatus.CREATED);
    }
}

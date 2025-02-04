package com.bilgeadam.service;

import com.bilgeadam.dto.request.EmailDto;
import com.bilgeadam.entity.EStatusEmail;
import com.bilgeadam.entity.Mail;
import com.bilgeadam.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class MailService {
    private final MailSender mailSender;
    private final MailRepository mailRepository;

    public Mail createEmail(EmailDto dto) {

        return Mail.builder()
                .emailFrom(dto.emailFrom())
                .emailTo(dto.emailTo())
                .ownerRef(dto.ownerRef())
                .sendDateEmail(LocalDateTime.now())
                .subject(dto.subject())
                .text(dto.text())
                .build();
    }

    public void sendEmail(Mail email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(email.getEmailFrom());
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());

            mailSender.send(message);

            email.setStatusEmail(EStatusEmail.SENT);
        } catch (Exception e) {
            email.setStatusEmail(EStatusEmail.ERROR);
        } finally {
            mailRepository.save(email);
        }
    }
}

package com.bilgeadam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
	
	private final JavaMailSender mailSender;
	
	public void sendFeedbackReceivedEmail(String toEmail, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("destek@company.com");
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(content);
		mailSender.send(message);
	}
}
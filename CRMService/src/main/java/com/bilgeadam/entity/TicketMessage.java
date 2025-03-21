package com.bilgeadam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "ticket_messages")
public class TicketMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	Long ticketId; // 📌 Ticket ile bağlantıyı ID üzerinden kuruyoruz
	
	String senderEmail; // 📌 Mesajı gönderen müşteri e-posta adresi
	
	String recipientEmail; // 📌 Destek ekibinin e-posta adresi
	
	String subject; // 📌 Mesajın konusu
	@Column(columnDefinition = "TEXT")
	String messageContent; // 📌 Mesaj içeriği
	
	LocalDateTime sentAt; // 📌 Mesajın alındığı tarih
}
package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.TicketCategory;
import com.bilgeadam.entity.enums.TicketPriority;
import com.bilgeadam.entity.enums.TicketStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String subject;
	
	@Enumerated(EnumType.STRING)
	TicketStatus ticketStatus;
	
	@Enumerated(EnumType.STRING)
	TicketPriority priority;
	
	@Enumerated(EnumType.STRING)
	TicketCategory category;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime closedAt; // Kapatılma tarihi
	
	@ElementCollection // Dosya yollarını tutan bir liste
	@CollectionTable(name = "ticket_attachments", joinColumns = @JoinColumn(name = "ticket_id"))
	@Column(name = "file_path")
	List<String> attachmentUrls;
	
	@Transient // DB de tutma , sadece DTO için
	List<TicketActivity> activities;
	
	String customerEmail;
	
	Long customerId;
	
	Long performerId;
	
	@Column(unique = true, nullable = false, updatable = false)
	String ticketNumber; // UUID numarası
	
	@PrePersist
	public void generateTicketNumber() {
		this.ticketNumber = String.valueOf(generateRandom5DigitNumber());
	}
	
	/** 📌 **5 haneli rastgele bir sayı üretir (10000 ile 99999 arasında) **/
	private int generateRandom5DigitNumber() {
		return (int) (Math.random() * 90000) + 10000;
	}
}
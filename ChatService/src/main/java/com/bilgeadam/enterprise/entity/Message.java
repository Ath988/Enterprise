package com.bilgeadam.enterprise.entity;

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
@Table(name = "tblmessage")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Column(nullable = false)
	private String content;
	@Column(nullable = false)
	private Long senderId;
	@Column(nullable = false)
	private String chatId;
	@Column(nullable = false)
	@Builder.Default
	private LocalDateTime timeStamp=LocalDateTime.now();
	@Enumerated(EnumType.STRING)
	private EMessageStatus messageStatus;
	@Builder.Default
	private boolean isDeleted = false;
}